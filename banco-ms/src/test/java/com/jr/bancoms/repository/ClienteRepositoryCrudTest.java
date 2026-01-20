package com.jr.bancoms.repository;

import com.jr.bancoms.domain.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class ClienteRepositoryCrudTest {

    @Autowired
    private ClienteRepository repo;

    private Cliente jose, marianela, juan;

    @BeforeEach
    void seed() {
        jose = new Cliente();
        jose.setNombre("Jose Lema");
        jose.setDireccion("Otavalo sn y principal");
        jose.setTelefono("098254785");
        jose.setContrasena("1234");
        jose.setEstado(true);
        repo.save(jose);

        marianela = new Cliente();
        marianela.setNombre("Marianela Montalvo");
        marianela.setDireccion("Amazonas y NNUU");
        marianela.setTelefono("097548965");
        marianela.setContrasena("5678");
        marianela.setEstado(true);
        repo.save(marianela);

        juan = new Cliente();
        juan.setNombre("Juan Osorio");
        juan.setDireccion("13 junio y Equinoccial");
        juan.setTelefono("098874587");
        juan.setContrasena("1245");
        juan.setEstado(true);
        repo.save(juan);
    }

    @Test
    void crear_y_listar_clientes() {
        List<Cliente> all = repo.findAll();
        assertThat(all).hasSize(3);
        assertThat(all).extracting(Cliente::getNombre)
                .containsExactlyInAnyOrder("Jose Lema","Marianela Montalvo","Juan Osorio");
    }

    @Test
    void obtener_por_id() {
        var id = marianela.getId();
        var found = repo.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().getDireccion()).isEqualTo("Amazonas y NNUU");
    }

    @Test
    void actualizar_cliente() {
        var id = jose.getId();
        var db = repo.findById(id).orElseThrow();
        db.setTelefono("000-111-222");
        db.setDireccion("Av. Siempre Viva 742");
        repo.save(db);

        var updated = repo.findById(id).orElseThrow();
        assertThat(updated.getTelefono()).isEqualTo("000-111-222");
        assertThat(updated.getDireccion()).isEqualTo("Av. Siempre Viva 742");
    }

    @Test
    void eliminar_cliente() {
        var id = juan.getId();
        repo.deleteById(id);
        assertThat(repo.findById(id)).isEmpty();
        assertThat(repo.findAll()).hasSize(2);
    }
}
