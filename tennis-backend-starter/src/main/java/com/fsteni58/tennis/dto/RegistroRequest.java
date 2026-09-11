package com.fsteni58.tennis.dto;

// Lo que llega desde el formulario de registro (Comprador US1)
public class RegistroRequest {
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol; // "comprador" o "proveedor"

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
