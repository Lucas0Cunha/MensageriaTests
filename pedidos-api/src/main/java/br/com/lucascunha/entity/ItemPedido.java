package br.com.lucascunha.entity;

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
