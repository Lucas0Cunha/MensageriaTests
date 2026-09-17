package br.com.lucascunha.pedidos.processador.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.lucascunha.pedidos.processador.entity.ItemPedido;
import br.com.lucascunha.pedidos.processador.repository.ProdutoRepository;

@Service 
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    void save(List<ItemPedido> itens) {
        
        itens.forEach(item -> {
            produtoRepository.save(item.getProduto());
        });
    }

}

