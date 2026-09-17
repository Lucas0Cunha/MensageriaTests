package br.com.lucascunha.pedidos.notificacao.entity;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@NoArgsConstructor 
public class ItemPedido {

    private UUID id = UUID.randomUUID();

    private Produto produto;

    private Integer quantidade;

}
