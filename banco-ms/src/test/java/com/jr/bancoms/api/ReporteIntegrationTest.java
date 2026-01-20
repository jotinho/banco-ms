package com.jr.bancoms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jr.bancoms.domain.Cliente;
import com.jr.bancoms.domain.Cuenta;
import com.jr.bancoms.domain.Movimiento;
import com.jr.bancoms.repository.ClienteRepository;
import com.jr.bancoms.repository.CuentaRepository;
import com.jr.bancoms.repository.MovimientoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReporteIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ClienteRepository clientes;
    @Autowired CuentaRepository cuentas;
    @Autowired MovimientoRepository movimientos;
    @Autowired ObjectMapper om;

    @Test
    void estadoDeCuentaYMovimientos() throws Exception {
        Cliente cli = new Cliente();
        cli.setNombre("John Rivera");
        cli.setDireccion("Calle 1");
        cli.setTelefono("555");
        cli.setContrasena("1234");
        cli.setEstado(true);
        cli = clientes.save(cli);

        Cuenta cta = new Cuenta();
        cta.setNumeroCuenta("478758");
        cta.setTipoCuenta("Ahorro");
        cta.setSaldo(new BigDecimal("2000"));
        cta.setEstado(true);
        cta.setCliente(cli);
        cta = cuentas.save(cta);

        // movimientos en enero
        Movimiento m1 = new Movimiento();
        m1.setCuenta(cta);
        m1.setFecha(LocalDateTime.of(2026, 1, 10, 10, 0));
        m1.setTipoMovimiento("Deposito");
        m1.setValor(new BigDecimal("600"));
        m1.setSaldo(new BigDecimal("2600"));
        movimientos.save(m1);

        Movimiento m2 = new Movimiento();
        m2.setCuenta(cta);
        m2.setFecha(LocalDateTime.of(2026, 1, 12, 14, 0));
        m2.setTipoMovimiento("Retiro");
        m2.setValor(new BigDecimal("-200"));
        m2.setSaldo(new BigDecimal("2400"));
        movimientos.save(m2);

        // realiza llamado al endpoint con rango 2026-01-01_2026-01-31
        String rango = "2026-01-01_2026-01-31";
        mvc.perform(get("/reportes")
                        .param("clienteId", cli.getId().toString())
                        .param("rango", rango))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(cli.getId().toString()))
                .andExpect(jsonPath("$.cuentas[0].numeroCuenta").value("478758"))
                .andExpect(jsonPath("$.cuentas[0].movimientos.length()").value(2))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].tipoMovimiento").value("Deposito"))
                .andExpect(jsonPath("$.cuentas[0].movimientos[1].tipoMovimiento").value("Retiro"));
    }
}
