package br.com.lucascunha.pedidos.processador.service;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.lucascunha.pedidos.processador.entity.ItemPedido;
import br.com.lucascunha.pedidos.processador.entity.Pedido;
import br.com.lucascunha.pedidos.processador.repository.ItemPedidoRepository;


@Service
public class ItemPedidoService {
    
    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ItemPedido> save(List<ItemPedido> itens) {
        
        return itemPedidoRepository.saveAll(itens);
    }

    void saveToUpdate (ItemPedido item) {
        itemPedidoRepository.save(item);
    }

    void updatedItemPedido(List<ItemPedido> itemPedidos, Pedido pedido) {
        itemPedidos.forEach(item -> {
            item.setPedido(pedido);
            saveToUpdate(item);
        });
    }

}
