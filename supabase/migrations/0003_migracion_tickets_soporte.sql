-- Administrador US10: canales de comunicación con compradores (sistema de tickets)

create table tickets_soporte (
    id uuid primary key default gen_random_uuid(),
    comprador_id uuid not null references usuarios(id),
    asunto varchar(150) not null,
    descripcion text not null,
    respuesta text,
    estado varchar(20) not null default 'abierto' check (estado in ('abierto', 'respondido', 'cerrado')),
    fecha_creacion timestamptz default now(),
    fecha_respuesta timestamptz
);

create index idx_tickets_comprador on tickets_soporte(comprador_id);
create index idx_tickets_estado on tickets_soporte(estado);
