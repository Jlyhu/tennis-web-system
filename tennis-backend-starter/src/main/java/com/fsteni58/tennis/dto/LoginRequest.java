package com.fsteni58.tennis.dto;

// Lo que llega desde el formulario de inicio de sesión (Comprador US2)
public class LoginRequest {
    private String correo;
    private String contrasena;

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
