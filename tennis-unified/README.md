# Backend único — Fsteni58

Un solo proyecto Maven/Spring Boot con las 3 historias del Sprint 1-2:
registro, login, inventario, y gestión de clientes (Administrador US5).

## Estructura (adoptando las capas que ya usaba Jero)

- controller/ — recibe las peticiones HTTP
- service/ — lógica de negocio y validaciones
- repository/ — el único lugar que habla SQL con Supabase
- model/ — representa las filas de las tablas
- dto/ — lo que entra y sale por la API
- exception/ — errores propios + manejador global (ManejadorExcepciones)
- config/ — configuración (CORS)

## Cómo correrlo

1. Copia .env.example a .env con tu contraseña real de Supabase.
2. mvn spring-boot:run
3. Abre http://localhost:8080 — ahí está la interfaz de las 4 funcionalidades.

## Endpoints

- POST /auth/register
- POST /auth/login
- POST /productos
- GET /admin/clientes
- GET /admin/clientes/{id}
- PUT /admin/clientes/{id}
- DELETE /admin/clientes/{id}  (desactivar, borrado lógico)
