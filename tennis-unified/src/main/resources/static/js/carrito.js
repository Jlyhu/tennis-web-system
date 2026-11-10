/*
 * Programa:     carrito.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica compartida del carrito en el navegador. Llama a los
 *               endpoints /carrito del backend, maneja el botón "Agregar al
 *               carrito" de las tarjetas, muestra avisos y mantiene el
 *               contador del menú. Se carga en todas las páginas donde hay
 *               carrito. Requiere cargar antes js/sesion.js y js/productos-ui.js.
 */

const RUTA_CARRITO = '/carrito';

/**
 * Devuelve la sesión solo si es de un comprador.
 * @returns {Object|null} sesión del comprador o null
 */
function sesionComprador() {
  const sesion = obtenerSesion();
  return sesion && sesion.rol === 'comprador' ? sesion : null;
}

/**
 * Llama a un endpoint del carrito. Agrega siempre el usuarioId de la sesión.
 * @param {string} ruta parte de la URL después de /carrito (puede ser "")
 * @param {string} metodo GET, POST, PATCH o DELETE
 * @param {Object} params parámetros de consulta (productoId, cantidad, etc.)
 * @returns {Promise<Object>} cuerpo JSON de la respuesta
 * @throws {Error} con el mensaje del backend si responde con error
 */
async function pedirCarrito(ruta, metodo, params) {
  const consulta = new URLSearchParams({ usuarioId: sesionComprador().id, ...params });
  const respuesta = await fetch(RUTA_CARRITO + ruta + '?' + consulta, { method: metodo });
  const datos = await respuesta.json().catch(() => ({}));
  if (!respuesta.ok) {
    throw new Error(datos.mensaje || datos.error || 'No se pudo completar la acción');
  }
  return datos;
}

/** @returns {Promise<Object>} el carrito del comprador */
function verCarrito() { return pedirCarrito('', 'GET', {}); }

/**
 * Agrega unidades de un producto al carrito.
 * @param {string} productoId id del producto
 * @param {number} cantidad unidades a agregar
 * @returns {Promise<Object>} carrito actualizado
 */
function agregarAlCarrito(productoId, cantidad) {
  return pedirCarrito('/items', 'POST', { productoId, cantidad });
}

/**
 * Cambia la cantidad de un producto del carrito.
 * @param {string} productoId id del producto
 * @param {number} cantidad nueva cantidad
 * @returns {Promise<Object>} carrito actualizado
 */
function cambiarCantidadCarrito(productoId, cantidad) {
  return pedirCarrito('/items/' + productoId, 'PATCH', { cantidad });
}

/**
 * Quita un producto del carrito.
 * @param {string} productoId id del producto
 * @returns {Promise<Object>} carrito actualizado
 */
function quitarDelCarrito(productoId) {
  return pedirCarrito('/items/' + productoId, 'DELETE', {});
}

/** @returns {Promise<Object>} carrito vacío */
function vaciarCarrito() { return pedirCarrito('', 'DELETE', {}); }

/**
 * Paga el carrito (simulado).
 * @param {string} metodoPago "nequi", "tarjeta" o "paypal"
 * @returns {Promise<Object>} mensaje, referencia, método, total y unidades
 */
function pagarCarrito(metodoPago) {
  return pedirCarrito('/pagar', 'POST', { metodoPago });
}

/**
 * Muestra un aviso flotante que se quita solo a los 3 segundos.
 * @param {string} texto mensaje a mostrar
 * @param {string} tipo "exito" o "error"
 */
function mostrarToast(texto, tipo) {
  let toast = document.getElementById('toast');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'toast';
    toast.className = 'toast';
    document.body.appendChild(toast);
  }
  toast.textContent = texto;
  toast.className = 'toast visible ' + (tipo || 'exito');
  clearTimeout(mostrarToast.temporizador);
  mostrarToast.temporizador = setTimeout(() => toast.classList.remove('visible'), 3000);
}

/**
 * Actualiza el número del ícono del carrito en el menú.
 * @param {number} cantidad unidades en el carrito
 */
function actualizarContador(cantidad) {
  document.querySelectorAll('.cart-count').forEach(el => { el.textContent = cantidad; });
}

/**
 * Lleva al login y, al iniciar sesión, regresa a la página actual.
 */
function irALoginYVolver() {
  let pagina = (window.location.pathname.split('/').pop() || 'index.html') + window.location.search;
  if (!/^[\w-]+\.html(\?[\w=&%-]*)?$/.test(pagina)) pagina = 'catalogo.html';
  window.location.href = 'login.html?volver=' + encodeURIComponent(pagina);
}

/**
 * Reemplaza el botón de carrito del menú por un enlace a carrito.html (en
 * cualquier página), lo oculta a proveedor y administrador, y carga el
 * contador cuando quien navega es un comprador.
 */
async function pintarCarritoEnMenu() {
  const acciones = document.querySelector('.nav-actions');
  if (!acciones) return;

  const sesion = obtenerSesion();
  document.querySelectorAll('.cart-btn').forEach(el => el.remove());
  if (sesion && sesion.rol !== 'comprador') return;

  const enlace = document.createElement('a');
  enlace.href = 'carrito.html';
  enlace.className = 'login-btn cart-btn';
  enlace.title = 'Carrito de compras';
  enlace.innerHTML = `
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M3 3h2l2.4 12.2a2 2 0 0 0 2 1.6h7.6a2 2 0 0 0 2-1.6L21 8H6" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
      <circle cx="9" cy="20" r="1.4" fill="currentColor"/>
      <circle cx="17" cy="20" r="1.4" fill="currentColor"/>
    </svg>
    <span class="cart-count">0</span>`;
  acciones.insertBefore(enlace, acciones.firstChild);

  if (sesion) {
    try { actualizarContador((await verCarrito()).cantidadTotal); } catch (e) { /* el contador queda en 0 */ }
  }
}

// Clic en "Agregar al carrito" (funciona para tarjetas que se pintan después).
document.addEventListener('click', async (evento) => {
  const boton = evento.target.closest('.btn-agregar');
  if (!boton) return;

  const sesion = obtenerSesion();
  if (!sesion) { irALoginYVolver(); return; }
  if (sesion.rol !== 'comprador') {
    mostrarToast('Solo los compradores pueden usar el carrito.', 'error');
    return;
  }

  boton.disabled = true;
  try {
    const carrito = await agregarAlCarrito(boton.dataset.productoId, 1);
    actualizarContador(carrito.cantidadTotal);
    mostrarToast('Producto agregado al carrito', 'exito');
  } catch (error) {
    mostrarToast(error.message, 'error');
  } finally {
    boton.disabled = false;
  }
});

document.addEventListener('DOMContentLoaded', pintarCarritoEnMenu);