package br.com.lucascunha.pedidos.notificacao.config;


import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration 
public class RabbitMQConfig {

    // Nome da exchange usada para publicar os pedidos. O nome no RabbitMq para a exchange é como se fosse o "endereço" para onde as mensagens são enviadas.
    @Value ("${rabbitmq.exchange.name}")
    private String exchangeName;

    // Nome da fila consumida pelo serviço de notificações. Mesmo conceito da de cima porém para filas.
    @Value ("${rabbitmq.queue.name}")
    private String queueName;

    // Nome da fila DLQ (Dead Letter Queue) usada para armazenar mensagens que não puderam ser processadas.
    @Value ("${rabbitmq.queue.dlq.name}")
    private String queueDlqName;

        @Value ("${rabbitmq.exchange.dlx.name}")
    private String exchangeDlxName;

    @Bean 
    public FanoutExchange pedidosDlxExchange() {
        return new FanoutExchange(exchangeDlxName);
    }

    @Bean 
    public Queue notificacaoDlqQueue() {
        return new Queue(queueDlqName);
    }

    @Bean 
    public Binding bindingDlq() {
        return BindingBuilder.bind(notificacaoDlqQueue()).to(pedidosDlxExchange());
    }


    @Bean 
    // Declara uma exchange fanout, que distribui mensagens para todas as filas vinculadas. Aqui ele define o tipo de exchange e atribui aquele endereço (exchangeName)
    public FanoutExchange pedidosExchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean 
    // Declara a fila, que armazena pedidos até o consumidor processá-los.
    public Queue notificacaoQueue() {

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", exchangeDlxName); // Define a exchange DLX para onde as mensagens rejeitadas serão enviadas. Ou seja, quando cair em excessão, esta linha diz: "envie a mensagem para a exchange DLX"

        return new Queue(queueName, true, false, false, args);
    }
    

    @Bean
    // Liga a fila à exchange para que ela receba as mensagens publicadas nela.
    public Binding binding() {
        return BindingBuilder.bind(notificacaoQueue()).to(pedidosExchange());
    }

    @Bean 
    // Usando a conexão do spring, o RabbitAdmin tem o objetivo de permitir que a aplicação possa utilizar recursos do RabbitMQ, como initialize, exchanges e filas. 
    // Ele também é responsável por declarar esses recursos no RabbitMQ.
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean 
    // Converte objetos Java em JSON ou JSON em Java antes da publicação das mensagens.
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean 
    // Componente usado pela aplicação para publicar mensagens em uma exchange. Foi utilizado por exemplo no PedidoService no rabbitTemplate.convertAndSend(exchangeName, "", pedido); 
    // Neste caso acima, ele usa o rabbitTemplate já configurado com a conexão do Spring (ConnectionFactory) e o conversor de mensagens (MessageConverter) para enviar o pedido para o endereço da exchange (exchangeName) e com a mensagem (pedido) convertida em JSON.
   
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean 
    // Inicializa no RabbitMQ as exchanges e demais recursos declarados pelo Spring.
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventListener(RabbitAdmin rabbitAdmin) {
     
        return event -> rabbitAdmin.initialize();//ao iniciar a aplicação ele inicia o RabbitAdmin e td q estiver declarado nele.
    }
}
