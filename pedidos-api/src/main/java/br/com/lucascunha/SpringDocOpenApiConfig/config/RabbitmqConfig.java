package br.com.lucascunha.SpringDocOpenApiConfig.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class RabbitmqConfig {

    // Nome da exchange usada para publicar os pedidos. O nome no RabbitMq para a exchange é como se fosse o "endereço" para onde as mensagens são enviadas.
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;


    @Bean 
    // Declara uma exchange fanout, que distribui mensagens para todas as filas vinculadas. Aqui ele define o tipo de exchange e atribui aquele endereço (exchangeName)
    public Exchange pedidosExchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    // Usando a conexão do spring, o RabbitAdmin tem o objetivo de permitir que a aplicação possa utilizar recursos do RabbitMQ, como initialize, exchanges e filas. Ele também é responsável por declarar esses recursos no RabbitMQ.
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean 
    // Converte objetos Java em JSON antes da publicação das mensagens.
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
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }


}
