# Backend Fsteni58 (Sprint 1)

Proyecto base en Spring Boot que conecta con Supabase y expone 3 funcionalidades:
registro, login, y registro de inventario.

## Cómo correrlo

1. Copia `.env.example` como `.env` y pon tu contraseña real de Supabase.
2. Abre una terminal en esta carpeta y corre:
   ```bash
   mvn spring-boot:run
   ```
3. El servidor queda escuchando en `http://localhost:8080`.

## Endpoints disponibles

### Registrarse (Comprador US1 / Proveedor)
```
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "nombre": "Valerie Garay",
  "correo": "valerie@ejemplo.com",
  "contrasena": "clave123",
  "rol": "comprador"
}
```

### Iniciar sesión (Comprador US2)
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "correo": "valerie@ejemplo.com",
  "contrasena": "clave123"
}
```

### Registrar un producto (Proveedor US1)
```
POST http://localhost:8080/productos
Content-Type: application/json

{
  "proveedorId": "el-id-del-proveedor-que-devolvio-el-registro",
  "nombre": "Raqueta de grafito",
  "descripcion": "Raqueta profesional",
  "categoria": "Equipamiento deportivo",
  "precio": 250000,
  "stock": 10
}
```

## Cómo probarlo con Postman (para la demo con el cliente)

1. Descarga Postman gratis (postman.com).
2. Crea una petición nueva tipo `POST` con la URL de cada endpoint de arriba.
3. En la pestaña "Body" elige "raw" y "JSON", pega el ejemplo correspondiente.
4. Dale "Send" y muestra la respuesta en pantalla.
5. Abre el Table Editor de Supabase en otra pestaña y muestra cómo aparece la fila nueva ahí — eso demuestra que la funcionalidad realmente está guardando datos reales, sin necesidad de tener la interfaz visual lista todavía.
