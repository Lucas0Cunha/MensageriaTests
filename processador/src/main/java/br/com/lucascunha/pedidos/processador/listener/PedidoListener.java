package br.com.lucascunha.pedidos.processador.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.lucascunha.pedidos.processador.entity.Pedido;
import br.com.lucascunha.pedidos.processador.enums.Status;
import br.com.lucascunha.pedidos.processador.service.PedidoService;

@Component 
public class PedidoListener {

    private final Logger logger = LoggerFactory.getLogger(PedidoListener.class);
    private final PedidoService pedidoService;

    public PedidoListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = "pedidos.v1.pedidos-criado.gerar-processamento") 
    public void salvarPedido(Pedido pedido) {
        pedido.setStatus(Status.PROCESSADO);
        pedidoService.save(pedido);
    }



    
}
