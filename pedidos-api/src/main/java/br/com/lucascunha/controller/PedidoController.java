package br.com.lucascunha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucascunha.entity.Pedido;
import br.com.lucascunha.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pedidos", description = "Recurso para criar novo pedido")
@RestController 
@RequestMapping ("/api/v1/pedidos")
public class PedidoController {


    private final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }//tbm posso usar o @Autowired, mas como só tem um construtor, o spring entende que é pra injetar a dependencia

    @Operation (summary = "Cria um novo pedido", description = "Cria um novo pedido com base nas informações fornecidas no corpo da requisição", 
        responses = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
        }
    )
    
    @PostMapping 
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        logger.info("Criando pedido: {}", pedido.toString());
        pedido = pedidoService.enfileirarPedido(pedido);            
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }


    
}
