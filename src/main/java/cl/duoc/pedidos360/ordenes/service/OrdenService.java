package cl.duoc.pedidos360.ordenes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.pedidos360.ordenes.dto.CrearOrdenRequest;
import cl.duoc.pedidos360.ordenes.dto.EventoPedidoCreado;
import cl.duoc.pedidos360.ordenes.model.Orden;
import cl.duoc.pedidos360.ordenes.model.OrdenItem;
import cl.duoc.pedidos360.ordenes.repository.OrdenRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenService {

    private static final String ESTADO_COMPLETADO = "COMPLETADO";

    private final OrdenRepository ordenRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingkey}")
    private String routingKey;

    public OrdenService(OrdenRepository ordenRepository, RabbitTemplate rabbitTemplate) {
        this.ordenRepository = ordenRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Orden crearOrden(CrearOrdenRequest request) {
        Orden orden = new Orden();
        orden.setClienteEmail(request.getClienteEmail());
        orden.setEstado(ESTADO_COMPLETADO);
        orden.setFechaCreacion(LocalDateTime.now());

        List<OrdenItem> items = request.getItems().stream()
                .map(itemRequest -> {
                    OrdenItem item = new OrdenItem();
                    item.setIdProducto(itemRequest.getIdProducto());
                    item.setCantidad(itemRequest.getCantidad());
                    item.setPrecioUnitario(itemRequest.getPrecioUnitario());
                    item.setOrden(orden);
                    return item;
                })
                .toList();

        orden.setItems(new ArrayList<>(items));
        orden.setTotal(items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum());

        Orden ordenGuardada = ordenRepository.save(orden);
        publicarEvento(ordenGuardada);
        return ordenGuardada;
    }

    public List<Orden> listarOrdenes() {
        return ordenRepository.findAll();
    }

    private void publicarEvento(Orden orden) {
        List<EventoPedidoCreado.ItemEvento> items = orden.getItems().stream()
                .map(item -> EventoPedidoCreado.ItemEvento.builder()
                        .idProducto(item.getIdProducto())
                        .cantidad(item.getCantidad())
                        .build())
                .toList();

        EventoPedidoCreado evento = EventoPedidoCreado.builder()
                .idOrden(orden.getId())
                .clienteEmail(orden.getClienteEmail())
                .total(orden.getTotal())
                .items(items)
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, evento);
        log.info("Evento PedidoCreado publicado | idOrden={} | clienteEmail={} | total={} | exchange={} | routingKey={}",
                evento.getIdOrden(), evento.getClienteEmail(), evento.getTotal(), exchange, routingKey);
    }

}