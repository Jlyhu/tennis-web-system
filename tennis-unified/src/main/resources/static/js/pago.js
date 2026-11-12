/*
 * Programa:     pago.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista pago.html. Muestra el resumen del
 *               carrito, deja elegir el método de pago (Nequi, tarjeta
 *               crédito/débito o PayPal) y llama a POST /carrito/pagar. El pago es
 *               una simulación: no se piden ni se guardan datos de pago.
 *               Al terminar muestra la confirmación con su referencia.
 *               Requiere js/sesion.js, js/productos-ui.js y js/carrito.js.
 */

const NOMBRES_METODO = {
  nequi: 'Nequi',
  tarjeta: 'Tarjeta crédito / débito',
  paypal: 'PayPal'
};

const vistaPago = document.getElementById('pago-formulario');
const vistaConfirmacion = document.getElementById('pago-confirmacion');
const resumenPago = document.getElementById('pago-resumen');
const totalPago = document.getElementById('pago-total');
const errorPago = document.getElementById('pago-error');
const botonPagar = document.getElementById('btn-pagar');

/**
 * Pinta el resumen del carrito en la pantalla de pago.
 * @param {Object} carrito respuesta de GET /carrito
 */
function pintarResumen(carrito) {
  resumenPago.innerHTML = carrito.items.map(i => `
    <li><span>${escaparHtml(i.nombre)} × ${i.cantidad}</span><strong>${formatearPrecio(i.subtotal)}</strong></li>
  `).join('');
    document.getElementById('pago-subtotal').textContent = formatearPrecio(carrito.total);
  document.getElementById('pago-iva').textContent = formatearPrecio(carrito.iva);
  totalPago.textContent = formatearPrecio(carrito.totalConIva);
}

/**
 * Muestra la confirmación de la compra.
 * @param {Object} recibo respuesta de POST /carrito/pagar
 */
function pintarConfirmacion(recibo) {
  document.getElementById('conf-referencia').textContent = recibo.referencia;
  document.getElementById('conf-metodo').textContent = NOMBRES_METODO[recibo.metodoPago] || recibo.metodoPago;
  document.getElementById('conf-unidades').textContent = recibo.cantidadProductos;
  document.getElementById('conf-total').textContent = formatearPrecio(recibo.total);
  document.getElementById('conf-factura').textContent = recibo.facturaNumero;
  document.getElementById('conf-iva').textContent = formatearPrecio(recibo.iva);
  vistaPago.hidden = true;
  vistaConfirmacion.hidden = false;
  actualizarContador(0);
}

botonPagar.addEventListener('click', async () => {
  const elegido = document.querySelector('input[name="metodo"]:checked');
  errorPago.textContent = '';
  if (!elegido) {
    errorPago.textContent = 'Elige un método de pago.';
    return;
  }
  botonPagar.disabled = true;
  try {
    pintarConfirmacion(await pagarCarrito(elegido.value));
  } catch (error) {
    errorPago.textContent = error.message;
    botonPagar.disabled = false;
  }
});

/** Revisa la sesión y carga el resumen. Si el carrito está vacío, lo avisa. */
async function iniciar() {
  const sesion = obtenerSesion();
  if (!sesion || sesion.rol !== 'comprador') {
    window.location.href = 'login.html?volver=pago.html';
    return;
  }
  try {
    const carrito = await verCarrito();
    actualizarContador(carrito.cantidadTotal);
    if (carrito.items.length === 0) {
      vistaPago.innerHTML = '<div class="aviso-pendiente">Tu carrito está vacío. <a href="catalogo.html" class="view-catalog">Ir al catálogo</a></div>';
      return;
    }
    pintarResumen(carrito);
  } catch (error) {
    vistaPago.innerHTML = '<div class="aviso-pendiente">No se pudo cargar tu compra. Intenta de nuevo.</div>';
  }
}

iniciar();