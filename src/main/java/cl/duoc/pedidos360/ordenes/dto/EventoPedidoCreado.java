package cl.duoc.pedidos360.ordenes.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoPedidoCreado {

    private Long idOrden;
    private String clienteEmail;
    private Double total;
    private List<ItemEvento> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEvento {

        private Long idProducto;
        private Integer cantidad;

    }

}