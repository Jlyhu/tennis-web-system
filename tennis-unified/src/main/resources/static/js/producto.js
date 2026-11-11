/*
 * Programa:     producto.js
 * Versión:      1.0
 * Fecha:        10/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista producto.html. Lee el id de la URL
 *               (producto.html?id=...), pide el detalle a GET /productos/{id}
 *               y muestra imagen, descripción, precio, stock y el proveedor
 *               que lo vende. El botón "Agregar al carrito" lo maneja
 *               carrito.js. Requiere js/sesion.js, js/productos-ui.js y
 *               js/carrito.js.
 */

const contenedorDetalle = document.getElementById('detalle-producto');

/**
 * Pinta el detalle de un producto.
 * @param {Object} p respuesta de GET /productos/{id}
 */
function pintarDetalle(p) {
  const categoria = NOMBRES_CATEGORIA[p.categoria] || p.categoria || '';
  const agotado = Number(p.stock) <= 0;
  const imagen = p.imagenUrl
    ? `<img src="${escaparHtml(p.imagenUrl)}" alt="${escaparHtml(p.nombre)}"
            onerror="this.parentElement.classList.add('sin-imagen')">`
    : '';

  document.title = p.nombre + ' — Fsteni58';
  contenedorDetalle.innerHTML = `
    <div class="detalle-imagen producto-imagen ${p.imagenUrl ? '' : 'sin-imagen'}">${imagen}</div>
    <div class="detalle-info">
      <span class="producto-categoria">${escaparHtml(categoria)}</span>
      <h1>${escaparHtml(p.nombre)}</h1>
      <p class="detalle-proveedor">Vendido por <strong>${escaparHtml(p.proveedorNombre)}</strong></p>
      <p class="detalle-descripcion">${escaparHtml(p.descripcion)}</p>
      <span class="producto-precio">${formatearPrecio(p.precio)}</span>
      <p class="detalle-stock ${agotado ? 'agotado' : ''}">
        ${agotado ? 'Producto agotado' : p.stock + ' unidades disponibles'}
      </p>
      <button type="button" class="btn-agregar" data-producto-id="${escaparHtml(p.id)}" ${agotado ? 'disabled' : ''}>
        ${agotado ? 'Agotado' : 'Agregar al carrito'}
      </button>
    </div>
  `;
}

/** Lee el id de la URL y carga el producto. */
async function iniciarDetalle() {
  const id = new URLSearchParams(window.location.search).get('id');
  if (!id || !/^[0-9a-fA-F-]{36}$/.test(id)) {
    contenedorDetalle.innerHTML = '<div class="aviso-pendiente">Producto no válido.</div>';
    return;
  }
  try {
    const respuesta = await fetch('/productos/' + encodeURIComponent(id));
    const datos = await respuesta.json().catch(() => ({}));
    if (!respuesta.ok) {
      contenedorDetalle.innerHTML =
        `<div class="aviso-pendiente">${escaparHtml(datos.mensaje || 'No se encontró el producto.')}</div>`;
      return;
    }
    pintarDetalle(datos);
  } catch (error) {
    contenedorDetalle.innerHTML = '<div class="aviso-pendiente">No se pudo conectar con el servidor.</div>';
  }
}

iniciarDetalle();