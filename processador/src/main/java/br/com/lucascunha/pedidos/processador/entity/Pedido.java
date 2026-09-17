package br.com.lucascunha.pedidos.processador.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.lucascunha.pedidos.processador.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "pedido")
@Data 
@NoArgsConstructor 
public class Pedido {

    @Id 
    private UUID id = UUID.randomUUID();

    private String cliente;

    @OneToMany (mappedBy = "pedido")
    private List<ItemPedido> itens = new ArrayList<>();

    @Column (name = "valor_total")
    private Double valorTotal;

    @Column (name = "email_notificacao")
    private String emailNotificacao;

    @Enumerated (EnumType.STRING)
    private Status status = Status.EM_PROCESSAMENTO;

    @JsonFormat (pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora = LocalDateTime.now();
    

}
