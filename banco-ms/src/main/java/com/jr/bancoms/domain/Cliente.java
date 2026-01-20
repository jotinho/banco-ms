package com.jr.bancoms.domain;

import jakarta.persistence.*;

@Entity @Table(name = "cliente")
public class Cliente extends Persona {
    @Column(nullable = false) private String contrasena;
    @Column(nullable = false) private Boolean estado;

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
