package com.jr.bancoms.api;

import com.jr.bancoms.domain.Cuenta;
import com.jr.bancoms.domain.Cliente;
import com.jr.bancoms.repository.CuentaRepository;
import com.jr.bancoms.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    private final CuentaRepository cuentas;
    private final ClienteRepository clientes;

    public CuentaController(CuentaRepository cuentas, ClienteRepository clientes) {
        this.cuentas = cuentas; this.clientes = clientes;
    }

    @GetMapping
    public List<Cuenta> list() { return cuentas.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> get(@PathVariable UUID id) {
        return cuentas.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cuenta> create(@RequestBody Cuenta c) {
        if (c.getCliente() != null && c.getCliente().getId() != null) {
            UUID clienteId = c.getCliente().getId();
            Optional<Cliente> cli = clientes.findById(clienteId);
            if (cli.isEmpty()) return ResponseEntity.badRequest().build();
            c.setCliente(cli.get());
        }
        return ResponseEntity.ok(cuentas.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> update(@PathVariable UUID id, @RequestBody Cuenta c) {
        return cuentas.findById(id).map(db -> {
            db.setNumeroCuenta(c.getNumeroCuenta());
            db.setTipoCuenta(c.getTipoCuenta());
            db.setSaldo(c.getSaldo());
            db.setEstado(c.getEstado());
            if (c.getCliente() != null && c.getCliente().getId() != null) {
                clientes.findById(c.getCliente().getId()).ifPresent(db::setCliente);
            }
            return ResponseEntity.ok(cuentas.save(db));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!cuentas.existsById(id)) return ResponseEntity.notFound().build();
        cuentas.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
