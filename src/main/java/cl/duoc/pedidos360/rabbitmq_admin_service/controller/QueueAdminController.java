package cl.duoc.pedidos360.rabbitmq_admin_service.controller;

import cl.duoc.pedidos360.rabbitmq_admin_service.dto.QueueRequest;
import cl.duoc.pedidos360.rabbitmq_admin_service.service.RabbitAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queues")
@RequiredArgsConstructor
public class QueueAdminController {

    private final RabbitAdminService rabbitAdminService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody QueueRequest request) {
        Queue queue = rabbitAdminService.crearCola(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("name", queue.getName(), "durable", queue.isDurable()));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> eliminar(@PathVariable String name) {
        boolean existia = rabbitAdminService.eliminarCola(name);
        return existia ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> listar() {
        return ResponseEntity.ok(rabbitAdminService.listarColas());
    }
}
