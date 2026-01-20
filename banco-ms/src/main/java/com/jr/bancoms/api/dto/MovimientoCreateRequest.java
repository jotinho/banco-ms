package com.jr.bancoms.api.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovimientoCreateRequest(
        @NotNull UUID cuentaId,
        @NotNull BigDecimal valor,
        String tipoMovimiento,
        LocalDateTime fecha
) {}
