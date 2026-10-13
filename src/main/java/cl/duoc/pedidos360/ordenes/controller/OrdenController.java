package cl.duoc.pedidos360.ordenes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.pedidos360.ordenes.dto.CrearOrdenRequest;
import cl.duoc.pedidos360.ordenes.model.Orden;
import cl.duoc.pedidos360.ordenes.service.OrdenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ordenes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @PostMapping
    public ResponseEntity<Orden> crearOrden(@Valid @RequestBody CrearOrdenRequest request) {
        Orden orden = ordenService.crearOrden(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    @GetMapping
    public List<Orden> listarOrdenes() {
        return ordenService.listarOrdenes();
    }

}