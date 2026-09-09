-- Sprint 1: autenticación (compradores) e inventario (proveedores)

-- Extensión para generar UUIDs
create extension if not exists "pgcrypto";

-- Tabla base de usuarios (compradores y proveedores)
create table usuarios (
    id uuid primary key default gen_random_uuid(),
    nombre varchar(150) not null,
    correo varchar(150) not null unique,
    contrasena_hash text not null,
    rol varchar(20) not null check (rol in ('comprador', 'proveedor', 'administrador')),
    fecha_registro timestamp with time zone default now()
);

-- Datos adicionales del proveedor mayorista (extiende usuarios cuando rol = 'proveedor')
create table proveedores (
    id uuid primary key default gen_random_uuid(),
    usuario_id uuid not null references usuarios(id) on delete cascade,
    nombre_empresa varchar(150) not null,
    telefono varchar(30),
    direccion varchar(200)
);

-- Inventario/productos que registra el proveedor (Proveedor - US1)
create table productos (
    id uuid primary key default gen_random_uuid(),
    proveedor_id uuid not null references proveedores(id) on delete cascade,
    nombre varchar(150) not null,
    descripcion text,
    categoria varchar(80),
    precio numeric(10,2) not null check (precio >= 0),
    stock integer not null default 0 check (stock >= 0),
    fecha_creacion timestamp with time zone default now()
);

-- Índices útiles para búsquedas frecuentes
create index idx_productos_proveedor on productos(proveedor_id);
create index idx_usuarios_correo on usuarios(correo);