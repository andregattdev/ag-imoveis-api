package com.agimoveis.api.model.enums;

public enum TipoUsuario {
    ADMIN("admin"),
    CORRETOR("corretor"),
    CLIENTE("cliente");

    private String role;

    TipoUsuario(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}