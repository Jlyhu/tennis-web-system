/*
 * Programa:     historial.js
 * Versión:      1.0
 * Fecha:        11/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista historial.html. Consulta las compras del
 *               comprador con sesión (GET /api/compras/mias) y las muestra
 *               de la más reciente a la más antigua, con su estado, sus
 *               productos y el total pagado (IVA incluido).
 *               Requiere js/sesion.js, js/productos-ui.js y js/carrito.js.
 */

const ETIQUETA_ESTADO = {
  PENDIENTE: 'Pendiente',
  EN_PROCESO: 'Pagado, en preparación',
  ENVIADO: 'Enviado',
  ENTREGADO: 'Entregado',
  CANCELADO: 'Cancelado'
};

const contenedorHistorial = document.getElementById('historial-lista');

/**
 * Pinta una compra: encabezado con fecha, estado y total, y sus productos.
 * @param {Object} compra {id, fecha, estado, total, productos[]}
 * @returns {string} HTML de la compra
 */
function tarjetaCompra(compra) {
  const fecha = new Date(compra.fecha).toLocaleString('es-CO', { dateStyle: 'medium', timeStyle: 'short' });
  const estado = ETIQUETA_ESTADO[compra.estado] || compra.estado;
  const productos = compra.productos.map(p => `
    <li><span>${escaparHtml(p.nombreProducto)} × ${p.cantidad}</span><strong>${formatearPrecio(p.subtotal)}</strong></li>
  `).join('');
  return `
    <div class="pago-caja">
      <h3>Compra del ${escaparHtml(fecha)}</h3>
      <ul class="pago-resumen">
        <li><span>Estado</span><strong>${escaparHtml(estado)}</strong></li>
        ${productos}
      </ul>
      <div class="carrito-total">
        <span>Total pagado (IVA incluido)</span>
        <strong>${formatearPrecio(compra.total)}</strong>
      </div>
    </div>
  `;
}

/** Revisa la sesión, consulta el historial y lo pinta. */
async function iniciarHistorial() {
  const sesion = obtenerSesion();
  if (!sesion || sesion.rol !== 'comprador') {
    window.location.href = 'login.html?volver=historial.html';
    return;
  }
  try {
    const respuesta = await fetch('/api/compras/mias');
    const compras = await respuesta.json().catch(() => []);
    if (!respuesta.ok) {
      throw new Error(compras.mensaje || 'No se pudo cargar tu historial');
    }
    if (compras.length === 0) {
      contenedorHistorial.innerHTML = '<div class="aviso-pendiente">Aún no tienes compras. <a href="catalogo.html" class="view-catalog">Ir al catálogo</a></div>';
      return;
    }
    contenedorHistorial.innerHTML = compras.map(tarjetaCompra).join('');
  } catch (error) {
    contenedorHistorial.innerHTML = '<div class="aviso-pendiente">' + escaparHtml(error.message) + '</div>';
  }
}

iniciarHistorial();