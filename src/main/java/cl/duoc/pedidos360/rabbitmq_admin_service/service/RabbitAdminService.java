package cl.duoc.pedidos360.rabbitmq_admin_service.service;

import cl.duoc.pedidos360.rabbitmq_admin_service.dto.QueueRequest;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;
    private final RestTemplate restTemplate;

    @Value("${rabbitmq.management.base-url}")
    private String managementBaseUrl;
    @Value("${rabbitmq.management.username}")
    private String managementUser;
    @Value("${rabbitmq.management.password}")
    private String managementPass;

    public RabbitAdminService(AmqpAdmin amqpAdmin, RestTemplate restTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.restTemplate = restTemplate;
    }

    public Queue crearCola(QueueRequest request) {
        Map<String, Object> args = new HashMap<>();
        if (request.getDeadLetterExchange() != null && !request.getDeadLetterExchange().isBlank()) {
            args.put("x-dead-letter-exchange", request.getDeadLetterExchange());
        }
        Queue queue = new Queue(request.getName(), request.getDurable(), false, false, args);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    public boolean eliminarCola(String nombre) {
        return amqpAdmin.deleteQueue(nombre);
    }

    public List<String> listarColas() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(managementUser, managementPass);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                managementBaseUrl + "/queues",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        return response.getBody() == null ? List.of() :
                response.getBody().stream()
                        .map(q -> (String) q.get("name"))
                        .collect(Collectors.toList());
    }
}
