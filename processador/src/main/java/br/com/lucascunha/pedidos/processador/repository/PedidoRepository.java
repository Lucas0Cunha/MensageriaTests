package br.com.lucascunha.pedidos.processador.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lucascunha.pedidos.processador.entity.Pedido;

@Repository 
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    
}
