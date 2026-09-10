-- 0002_sync_roles.sql
-- Sincroniza la base de datos real con el esquema de roles normalizados + soft delete

drop table if exists productos;
drop table if exists proveedores;
drop table if exists usuarios;

create extension if not exists "pgcrypto";

create table roles (
    id uuid primary key default gen_random_uuid(),
    nombre_rol varchar(30) not null unique check (nombre_rol in ('comprador', 'proveedor', 'administrador'))
);

insert into roles (nombre_rol) values ('comprador'), ('proveedor'), ('administrador');

create table usuarios (
    id uuid primary key default gen_random_uuid(),
    nombre varchar(150) not null,
    correo varchar(150) not null unique,
    contrasena_hash text not null,
    rol_id uuid not null references roles(id),
    activo boolean not null default true,
    fecha_registro timestamp with time zone default now()
);

create table proveedores (
    id uuid primary key default gen_random_uuid(),
    usuario_id uuid not null unique references usuarios(id),
    nombre_empresa varchar(150) not null,
    nit varchar(30),
    telefono varchar(30),
    direccion varchar(200),
    activo boolean not null default true
);

create table productos (
    id uuid primary key default gen_random_uuid(),
    proveedor_id uuid not null references proveedores(id),
    nombre varchar(150) not null,
    descripcion text,
    categoria varchar(80),
    precio numeric(10,2) not null check (precio >= 0),
    stock integer not null default 0 check (stock >= 0),
    activo boolean not null default true,
    fecha_creacion timestamp with time zone default now()
);

create index idx_usuarios_rol on usuarios(rol_id);
create index idx_usuarios_correo on usuarios(correo);
create index idx_productos_proveedor on productos(proveedor_id);
