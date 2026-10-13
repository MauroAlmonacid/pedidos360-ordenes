package cl.duoc.pedidos360.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.pedidos360.ordenes.model.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

}