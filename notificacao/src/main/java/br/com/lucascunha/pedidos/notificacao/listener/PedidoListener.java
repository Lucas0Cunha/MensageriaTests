package br.com.lucascunha.pedidos.notificacao.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.lucascunha.pedidos.notificacao.entity.Pedido;
import br.com.lucascunha.pedidos.notificacao.service.EmailService;

@Component 
public class PedidoListener {

    private final Logger logger = LoggerFactory.getLogger(PedidoListener.class);
    private final EmailService emailService;

    public PedidoListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "pedidos.v1.pedidos-criado.gerar-notificacao") //O @RabbitListener diz que o metodo abaixo vai ser chamado quando uma mensagem chegar na fila "pedidos.v1.pedidos-criado.gerar-notificacao"
    //como a exchange é fanout, todas as filas esperam as mensagens (esta conexão é feita pelo binding declarado no RabbitMQConfig)
    public void enviarNotificacao(Pedido pedido) {
        
        if(pedido.getValorTotal() > 2000){
            throw new RuntimeException("Valor do pedido maior que 2000, não é possível enviar a notificação.");
        }//Se o valor do pedido for maior que 2000, uma exceção é lançada, o que faz com que a mensagem seja rejeitada e enviada para a DLQ (Dead Letter Queue) configurada no RabbitMQConfig.

        emailService.enviarEmail(pedido);
        logger.info("Notificação gerada: {}", pedido.toString());
    }

}
