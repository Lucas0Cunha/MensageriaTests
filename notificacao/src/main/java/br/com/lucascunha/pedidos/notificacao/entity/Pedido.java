package br.com.lucascunha.pedidos.notificacao.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.lucascunha.pedidos.notificacao.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class Pedido {


    private UUID id = UUID.randomUUID();

    private String cliente;

    private List<ItemPedido> itens = new ArrayList<>();

    private Double valorTotal;

    private String emailNotificacao;

    private Status status = Status.EM_PROCESSAMENTO;

    @JsonFormat (pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora = LocalDateTime.now();
    

}
