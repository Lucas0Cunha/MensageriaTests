package br.com.lucascunha.entity;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class Produto {


    private UUID id = UUID.randomUUID();

    private String nome;

    private Double valor;
}
