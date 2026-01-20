package com.jr.bancoms.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReporteResponse(
        UUID idCliente,
        String nombreCliente,
        String rango,
        List<CuentaConMovimientos> cuentas
) {
    public record CuentaConMovimientos(
            UUID idCuenta,
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldo,
            List<MovimientoItem> movimientos
    ) {}
    public record MovimientoItem(
            UUID id,
            LocalDateTime fecha,
            String tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo
    ) {}
}
