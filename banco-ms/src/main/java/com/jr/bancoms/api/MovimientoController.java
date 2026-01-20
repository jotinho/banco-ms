package com.jr.bancoms.api;

import com.jr.bancoms.api.dto.MovimientoCreateRequest;
import com.jr.bancoms.domain.Cuenta;
import com.jr.bancoms.domain.Movimiento;
import com.jr.bancoms.repository.CuentaRepository;
import com.jr.bancoms.repository.MovimientoRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoRepository movimientos;
    private final CuentaRepository cuentas;

    public MovimientoController(MovimientoRepository movimientos, CuentaRepository cuentas) {
        this.movimientos = movimientos; this.cuentas = cuentas;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody MovimientoCreateRequest req) {
        UUID cuentaId = req.cuentaId();
        Cuenta cuenta = cuentas.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no existe: " + cuentaId));

        BigDecimal saldoActual = cuenta.getSaldo();
        BigDecimal nuevoSaldo  = saldoActual.add(req.valor());

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Saldo no disponible"));
        }

        Movimiento mov = new Movimiento();
        mov.setCuenta(cuenta);
        mov.setFecha(req.fecha() != null ? req.fecha() : LocalDateTime.now());

        String tipo = (req.tipoMovimiento() != null) ? req.tipoMovimiento() :
                (req.valor().signum() >= 0 ? "Deposito" : "Retiro");
        mov.setTipoMovimiento(tipo);
        mov.setValor(req.valor());
        mov.setSaldo(nuevoSaldo);

        movimientos.save(mov);

        cuenta.setSaldo(nuevoSaldo);
        cuentas.save(cuenta);

        return ResponseEntity.status(HttpStatus.CREATED).body(mov);
    }

    @GetMapping
    public java.util.List<Movimiento> listar() {
        return movimientos.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> obtener(@PathVariable UUID id) {
        return movimientos.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
