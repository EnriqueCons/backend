package com.ipn.mx.domain.repository;

import com.ipn.mx.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query("SELECT p FROM Pedido p WHERE p.comprador.id_Comprador = :idComprador")
    List<Pedido> findPedidosByCompradorId(@Param("idComprador") Integer idComprador);


    @Query("SELECT p FROM Pedido p WHERE p.cafeteria.idCafeteria = :idCafeteria ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByCafeteriaIdCafeteria(@Param("idCafeteria") Integer idCafeteria);

}
