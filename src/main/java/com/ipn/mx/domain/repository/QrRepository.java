package com.ipn.mx.domain.repository;

import com.ipn.mx.domain.entity.Comprador;
import com.ipn.mx.domain.entity.QR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrRepository extends JpaRepository<QR, Integer> {
    Optional<QR> findByComprador(Comprador comprador);
}