/*
 * Programa:     productos-ui.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Piezas compartidas para mostrar productos: carga del
 *               catálogo desde GET /productos, formato de precio y
 *               tarjeta HTML de cada producto. Lo usan catalogo.html
 *               (catalogo.js) y la sección "Productos destacados" del
 *               index (destacados.js).
 */

// Nombre legible de cada categoría guardada en la base de datos.
const NOMBRES_CATEGORIA = {
  raquetas: 'Raquetas Premium',
  calzado: 'Zapatillas & Calzado',
  accesorios: 'Accesorios & Pelotas'
};

/**
 * Escapa los caracteres especiales de HTML de un texto, para poder
 * insertarlo con seguridad en el DOM con innerHTML.
 * @param {*} texto valor a escapar (null o undefined se vuelven texto vacío)
 * @returns {string} texto seguro
 */
function escaparHtml(texto) {
  const reemplazos = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' };
  return String(texto ?? '').replace(/[&<>"']/g, c => reemplazos[c]);
}

/**
 * Da formato de pesos colombianos a un número.
 * @param {number} valor precio sin formato
 * @returns {string} por ejemplo "$489.900 COP"
 */
function formatearPrecio(valor) {
  return '$' + Number(valor).toLocaleString('es-CO') + ' COP';
}

/**
 * Pide al backend los productos activos del catálogo.
 * @returns {Promise<Array>} lista de productos (id, nombre, descripcion,
 *          categoria, precio, stock, imagenUrl)
 * @throws {Error} si el backend responde con error
 */
async function cargarProductos() {
  const respuesta = await fetch('/productos');
  if (!respuesta.ok) throw new Error('No se pudo cargar el catálogo');
  return respuesta.json();
}

/**
 * Arma el HTML de la tarjeta de un producto. Si el producto no tiene
 * imagen, o el archivo no existe, la tarjeta muestra un fondo crema.
 * @param {Object} p producto devuelto por el backend
 * @returns {string} HTML de la tarjeta
 */
function tarjetaProducto(p) {
  const categoria = NOMBRES_CATEGORIA[p.categoria] || p.categoria || '';
  const imagen = p.imagenUrl
    ? `<img src="${escaparHtml(p.imagenUrl)}" alt="${escaparHtml(p.nombre)}"
            onerror="this.parentElement.classList.add('sin-imagen')">`
    : '';

  return `
    <article class="producto">
      <div class="producto-imagen ${p.imagenUrl ? '' : 'sin-imagen'}">${imagen}</div>
      <div class="producto-info">
        <span class="producto-categoria">${escaparHtml(categoria)}</span>
        <h3 class="producto-nombre">${escaparHtml(p.nombre)}</h3>
        <p class="producto-descripcion">${escaparHtml(p.descripcion)}</p>
        <span class="producto-precio">${formatearPrecio(p.precio)}</span>
      </div>
    </article>
  `;
}