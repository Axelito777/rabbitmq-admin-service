package cl.duoc.pedidos360.rabbitmq_admin_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
public class QueueRequest {
    @NotBlank(message = "El nombre de la cola no puede estar vacío")
    private String name;

    private Boolean durable = true;
    private String deadLetterExchange; // opcional
}
