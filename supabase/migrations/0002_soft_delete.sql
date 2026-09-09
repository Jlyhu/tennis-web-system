-- Sprint: implementar borrado lógico (soft delete) en vez de DELETE físico

-- Agregar columna de estado activo/inactivo a usuarios
alter table usuarios add column activo boolean not null default true;

-- Agregar columna de estado activo/inactivo a proveedores
alter table proveedores add column activo boolean not null default true;

-- Agregar columna de estado activo/inactivo a productos
alter table productos add column activo boolean not null default true;

-- Quitar el "on delete cascade": ya no queremos que borrar un usuario
-- borre en cascada sus productos. Ahora todo se maneja con "activo".
alter table proveedores drop constraint proveedores_usuario_id_fkey;
alter table proveedores add constraint proveedores_usuario_id_fkey
    foreign key (usuario_id) references usuarios(id);

alter table productos drop constraint productos_proveedor_id_fkey;
alter table productos add constraint productos_proveedor_id_fkey
    foreign key (proveedor_id) references proveedores(id);
