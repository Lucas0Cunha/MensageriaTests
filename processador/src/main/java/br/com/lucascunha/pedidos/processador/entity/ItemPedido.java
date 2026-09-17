package br.com.lucascunha.pedidos.processador.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@NoArgsConstructor 
@Entity
@Table (name = "item_pedido")
public class ItemPedido {

    @Id 
    private UUID id = UUID.randomUUID();

    @ManyToOne 
    private Produto produto;

    private Integer quantidade;

    @ManyToOne 
    private Pedido pedido;

}
