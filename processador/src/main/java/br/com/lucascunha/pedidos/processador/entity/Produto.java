package br.com.lucascunha.pedidos.processador.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@Entity 
@Table (name = "produto")
public class Produto {

    @Id 
    private UUID id = UUID.randomUUID();

    private String nome;

    private Double valor;
}
