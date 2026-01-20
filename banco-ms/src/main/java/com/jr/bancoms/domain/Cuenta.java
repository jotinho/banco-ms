package com.jr.bancoms.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name="cuenta")
public class Cuenta {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "numero", unique = true, nullable=false) private String numeroCuenta;
    @Column(name = "tipo", nullable=false) private String tipoCuenta;
    @Column(nullable=false) private BigDecimal saldo = BigDecimal.ZERO;
    @Column(nullable=false) private Boolean estado = Boolean.TRUE;
    @JoinColumn(name = "id_cliente") @ManyToOne(optional=false) private Cliente cliente;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
