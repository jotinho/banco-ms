package com.jr.bancoms.repository;

import com.jr.bancoms.domain.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

public interface MovimientoRepository extends JpaRepository<Movimiento, UUID> {
    List<Movimiento> findByCuentaIdAndFechaBetween(UUID cuentaId, LocalDateTime inicio, LocalDateTime fin);
}
