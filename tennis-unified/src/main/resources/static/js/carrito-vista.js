/*
 * Programa:     carrito-vista.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista carrito.html. Muestra los productos del
 *               comprador con su cantidad y subtotal, permite sumar, restar,
 *               quitar y vaciar, y calcula el total. Si no hay sesión de
 *               comprador, lleva al login y regresa al carrito.
 *               Requiere js/sesion.js, js/productos-ui.js y js/carrito.js.
 */

const listaCarrito = document.getElementById('carrito-lista');
const resumenCarrito = document.getElementById('carrito-resumen');
const totalCarrito = document.getElementById('carrito-total');
const accionesCarrito = document.getElementById('carrito-acciones');

/**
 * Arma el HTML de un producto del carrito.
 * @param {Object} item ítem devuelto por el backend
 * @returns {string} HTML del ítem
 */
function itemCarrito(item) {
  const id = escaparHtml(item.productoId);
  const imagen = item.imagenUrl
    ? `<img src="${escaparHtml(item.imagenUrl)}" alt="${escaparHtml(item.nombre)}"
            onerror="this.parentElement.classList.add('sin-imagen')">`
    : '';
  return `
    <article class="carrito-item">
      <div class="carrito-img ${item.imagenUrl ? '' : 'sin-imagen'}">${imagen}</div>
      <div class="carrito-datos">
        <h3>${escaparHtml(item.nombre)}</h3>
        <span>${formatearPrecio(item.precioUnitario)} c/u</span>
      </div>
      <div class="carrito-cantidad">
        <button type="button" class="cantidad-btn" data-accion="menos" data-producto-id="${id}"
                data-cantidad="${item.cantidad}" ${item.cantidad <= 1 ? 'disabled' : ''}>−</button>
        <span>${item.cantidad}</span>
        <button type="button" class="cantidad-btn" data-accion="mas" data-producto-id="${id}"
                data-cantidad="${item.cantidad}" ${item.cantidad >= item.stock ? 'disabled' : ''}>+</button>
      </div>
      <strong class="carrito-subtotal">${formatearPrecio(item.subtotal)}</strong>
      <button type="button" class="btn-quitar" data-accion="quitar" data-producto-id="${id}">Quitar</button>
    </article>`;
}

/**
 * Pinta el carrito completo (o el aviso de carrito vacío).
 * @param {Object} carrito respuesta de GET /carrito
 */
function pintarCarrito(carrito) {
  actualizarContador(carrito.cantidadTotal);
  const vacio = carrito.items.length === 0;

  listaCarrito.innerHTML = vacio
    ? '<div class="aviso-pendiente">Tu carrito está vacío. <a href="catalogo.html" class="view-catalog">Ir al catálogo</a></div>'
    : carrito.items.map(itemCarrito).join('');
  resumenCarrito.hidden = vacio;
  accionesCarrito.hidden = vacio;
  totalCarrito.textContent = formatearPrecio(carrito.total);
}

// Botones de cada ítem: sumar, restar y quitar.
listaCarrito.addEventListener('click', async (evento) => {
  const boton = evento.target.closest('button[data-accion]');
  if (!boton) return;

  const id = boton.dataset.productoId;
  const cantidad = Number(boton.dataset.cantidad);
  try {
    let carrito;
    if (boton.dataset.accion === 'mas') carrito = await cambiarCantidadCarrito(id, cantidad + 1);
    else if (boton.dataset.accion === 'menos') carrito = await cambiarCantidadCarrito(id, cantidad - 1);
    else carrito = await quitarDelCarrito(id);
    pintarCarrito(carrito);
  } catch (error) {
    mostrarToast(error.message, 'error');
  }
});

document.getElementById('btn-vaciar').addEventListener('click', async () => {
  if (!confirm('¿Quieres vaciar el carrito?')) return;
  try {
    pintarCarrito(await vaciarCarrito());
  } catch (error) {
    mostrarToast(error.message, 'error');
  }
});

/** Revisa la sesión y carga el carrito. */
async function iniciar() {
  const sesion = obtenerSesion();
  if (!sesion || sesion.rol !== 'comprador') {
    window.location.href = 'login.html?volver=carrito.html';
    return;
  }
  try {
    pintarCarrito(await verCarrito());
  } catch (error) {
    listaCarrito.innerHTML = '<div class="aviso-pendiente">No se pudo cargar tu carrito. Intenta de nuevo.</div>';
  }
}

iniciar();