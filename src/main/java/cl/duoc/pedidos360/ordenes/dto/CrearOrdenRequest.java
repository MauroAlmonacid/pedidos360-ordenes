package cl.duoc.pedidos360.ordenes.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearOrdenRequest {

    @NotBlank(message = "clienteEmail es obligatorio")
    private String clienteEmail;

    @NotNull(message = "items es obligatorio")
    @Valid
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {

        @NotNull(message = "idProducto es obligatorio")
        private Long idProducto;

        @NotNull(message = "cantidad es obligatorio")
        private Integer cantidad;

        @NotNull(message = "precioUnitario es obligatorio")
        private Double precioUnitario;

    }

}