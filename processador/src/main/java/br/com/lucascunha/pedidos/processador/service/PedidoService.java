package br.com.lucascunha.pedidos.processador.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.lucascunha.pedidos.processador.entity.ItemPedido;
import br.com.lucascunha.pedidos.processador.entity.Pedido;
import br.com.lucascunha.pedidos.processador.repository.PedidoRepository;


@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoService itemPedidoService;
    private final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService, ItemPedidoService itemPedidoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
    }

    public void save(Pedido pedido) {

        // Salva o produto antes de salvar o pedido (itens do pedido são os produtos)
        produtoService.save(pedido.getItens());

        // Salva os itens do pedido e atribui a itemPedidos. Neste momento os itens ainda não possuem o pedido associado, pois o pedido ainda não foi salvo.
        List<ItemPedido> itemPedidos = itemPedidoService.save(pedido.getItens());

        // Salva o pedido, não poderia ter sido salvo antes, pois os itens do pedido ainda não estavam salvos e o pedido possui uma relação com os itens.
        pedidoRepository.save(pedido);

        // Atualiza os itens do pedido com o pedido salvo, para que a relação entre pedido e itens seja estabelecida.
        itemPedidoService.updatedItemPedido(itemPedidos, pedido);

        logger.info("Pedido salvo com sucesso: {}", pedido);
    }

}
