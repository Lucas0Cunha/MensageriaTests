package br.com.lucascunha.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.lucascunha.entity.Pedido;

@Service 
public class PedidoService {
    

    private final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;


    private final RabbitTemplate rabbitTemplate;

    public PedidoService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public Pedido enfileirarPedido(Pedido pedido) {
        logger.info("Enfileirando pedido: {}", pedido);
        // Ele usa o rabbitTemplate já configurado com a conexão do Spring (ConnectionFactory) e o conversor de mensagens (MessageConverter) para enviar o pedido para o endereço da exchange (exchangeName) e com a mensagem (pedido) convertida em JSON.
        rabbitTemplate.convertAndSend(exchangeName, "", pedido);
        return pedido;
    }


}
