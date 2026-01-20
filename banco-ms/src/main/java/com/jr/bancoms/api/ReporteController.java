package com.jr.bancoms.api;

import com.jr.bancoms.api.dto.ReporteResponse;
import com.jr.bancoms.domain.Cuenta;
import com.jr.bancoms.domain.Movimiento;
import com.jr.bancoms.domain.Cliente;
import com.jr.bancoms.repository.CuentaRepository;
import com.jr.bancoms.repository.MovimientoRepository;
import com.jr.bancoms.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class ReporteController {

    private final ClienteRepository clientes;
    private final CuentaRepository cuentas;
    private final MovimientoRepository movimientos;

    public ReporteController(ClienteRepository clientes, CuentaRepository cuentas, MovimientoRepository movimientos) {
        this.clientes = clientes;
        this.cuentas = cuentas;
        this.movimientos = movimientos;
    }

    @GetMapping("/reportes")
    public ResponseEntity<?> estadoDeCuenta(
            @RequestParam UUID clienteId,
            @RequestParam String rango
    ) {
        // valida y realiza el parseo del rango de fechas
        String[] parts = rango.split("_");
        if (parts.length != 2) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Formato de fecha inválido. Use YYYY-MM-DD_YYYY-MM-DD"));
        }
        LocalDate inicio, fin;
        try {
            inicio = LocalDate.parse(parts[0].trim());
            fin = LocalDate.parse(parts[1].trim());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Fechas inválidas. Use YYYY-MM-DD_YYYY-MM-DD"));
        }
        if (fin.isBefore(inicio)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "El fin del rango no puede ser anterior al inicio"));
        }
        LocalDateTime inicioTime = inicio.atStartOfDay();
        LocalDateTime finTime = fin.atTime(LocalTime.MAX);

        // info del cliente
        Cliente cli = clientes.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no existe: " + clienteId));

        // cuentas del cliente
        List<Cuenta> cuentasCliente = cuentas.findByClienteId(clienteId);

        // movimientos de cada cuenta dentro del rango
        List<ReporteResponse.CuentaConMovimientos> cuentasDto = cuentasCliente.stream()
                .map(c -> {
                    List<Movimiento> movs = movimientos
                            .findByCuentaIdAndFechaBetween(c.getId(), inicioTime, finTime);
                    List<ReporteResponse.MovimientoItem> items = movs.stream()
                            .map(m -> new ReporteResponse.MovimientoItem(
                                    m.getId(), m.getFecha(), m.getTipoMovimiento(),
                                    m.getValor(), m.getSaldo()
                            )).collect(Collectors.toList());
                    BigDecimal saldoActual = c.getSaldo() == null ? BigDecimal.ZERO : c.getSaldo();
                    return new ReporteResponse.CuentaConMovimientos(
                            c.getId(), c.getNumeroCuenta(), c.getTipoCuenta(), saldoActual, items
                    );
                }).collect(Collectors.toList());

        // prepara la respuesta
        ReporteResponse resp = new ReporteResponse(
                cli.getId(),
                cli.getNombre(),
                rango,
                cuentasDto
        );
        return ResponseEntity.ok(resp);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
