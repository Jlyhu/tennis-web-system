# Registro de usuarios (comprador) — Backend Spring Boot + Supabase

## Qué hace
- Expone `POST /api/usuarios/registro`.
- Valida campos obligatorios y formato de correo (en el DTO y en el service).
- Verifica que el correo no esté repetido antes de insertar.
- Hashea la contraseña con **BCrypt** antes de guardarla (nunca texto plano).
- Inserta el usuario en la tabla `usuarios` de Supabase con `rol_id` apuntando al rol `comprador`.
- Sirve el formulario de registro (`registro.html`) ya conectado al endpoint.

## 1. Configurar la conexión a Supabase
En Supabase: **Project Settings → Database → Connection string**.

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://db.<TU-PROYECTO>.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=${SUPABASE_DB_PASSWORD:coloca_tu_password_aqui}
```

**No subas tu contraseña real al repositorio.** Corre la app pasando la variable de entorno:

```bash
# Mac/Linux
export SUPABASE_DB_PASSWORD=tu_password_real
mvn spring-boot:run

# Windows (PowerShell)
$env:SUPABASE_DB_PASSWORD="tu_password_real"
mvn spring-boot:run
```

## 2. Ejecutar
```bash
mvn spring-boot:run
```
La app queda en `http://localhost:8080`.

- Formulario: `http://localhost:8080/registro.html`
- Endpoint: `POST http://localhost:8080/api/usuarios/registro`

## 3. Probar desde el celular
El celular y el computador deben estar en la **misma red Wi-Fi**.

1. En el computador, averigua tu IP local (Windows: `ipconfig`, Mac/Linux: `ifconfig` o `ip a`), algo como `192.168.1.25`.
2. En el celular abre: `http://192.168.1.25:8080/registro.html`.
3. Si no carga, revisa el firewall del computador (debe permitir conexiones entrantes al puerto 8080).

Para probar en internet real (no solo la misma red), lo más simple es desplegar el jar en un servicio como Render, Railway o Fly.io, y usar esa URL pública en vez de `localhost`.

## 4. Correr las pruebas unitarias
```bash
mvn test
```
Cubre: registro válido, correo duplicado, nombre/correo/contraseña faltantes, correo con formato inválido, contraseña corta.

## 5. Probar el endpoint manualmente (sin el formulario)
```bash
curl -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laura Gómez","correo":"laura@example.com","contrasena":"clave12345"}'
```

Respuesta esperada (201):
```json
{
  "id": "....",
  "nombre": "Laura Gómez",
  "correo": "laura@example.com",
  "rol": "comprador",
  "mensaje": "Registro exitoso"
}
```

Si repites el mismo correo, responde 409:
```json
{ "error": "El correo ya está registrado: laura@example.com" }
```
