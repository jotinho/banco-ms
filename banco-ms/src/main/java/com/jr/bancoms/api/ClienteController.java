package com.jr.bancoms.api;

import com.jr.bancoms.domain.Cliente;
import com.jr.bancoms.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repo;
    public ClienteController(ClienteRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Cliente> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> get(@PathVariable UUID id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente create(@RequestBody Cliente c) { return repo.save(c); }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable UUID id, @RequestBody Cliente c) {
        return repo.findById(id).map(db -> {
            db.setNombre(c.getNombre());
            db.setGenero(c.getGenero());
            db.setEdad(c.getEdad());
            db.setIdentificacion(c.getIdentificacion());
            db.setDireccion(c.getDireccion());
            db.setTelefono(c.getTelefono());
            db.setContrasena(c.getContrasena());
            db.setEstado(c.getEstado());
            return ResponseEntity.ok(repo.save(db));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
