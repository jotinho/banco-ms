package com.jr.bancoms.repository;

import com.jr.bancoms.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(UUID clienteId);
}

