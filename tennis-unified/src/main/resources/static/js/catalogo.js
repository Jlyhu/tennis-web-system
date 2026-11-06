/*
 * Programa:     catalogo.js
 * Versión:      2.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista catalogo.html. Carga los productos
 *               desde el backend (GET /productos) y permite filtrarlos por
 *               categoría. La categoría inicial se toma del parámetro
 *               ?categoria= de la URL (raquetas, calzado o accesorios), así
 *               los enlaces del index llegan ya filtrados.
 * Cambios v2.0: los productos ya no están escritos a mano; vienen de la
 *               base de datos. Requiere cargar js/productos-ui.js antes
 *               que este archivo.
 */

const grilla = document.getElementById('grilla');
const titulo = document.getElementById('catalogo-titulo');
const resumen = document.getElementById('resumen');
const botonesFiltro = document.querySelectorAll('.filtro');

// Productos que devuelve el backend; se llenan al abrir la página.
let productos = [];

/**
 * Lee el parámetro ?categoria= de la URL.
 * @returns {string} "raquetas", "calzado", "accesorios", o "todos" si no
 *          viene el parámetro o trae un valor que no existe
 */
function categoriaDesdeUrl() {
  const valor = new URLSearchParams(window.location.search).get('categoria');
  return NOMBRES_CATEGORIA[valor] ? valor : 'todos';
}

/**
 * Pinta en pantalla los productos de una categoría y marca el filtro activo.
 * @param {string} categoria "todos" o una llave de NOMBRES_CATEGORIA
 */
function pintar(categoria) {
  const lista = categoria === 'todos'
    ? productos
    : productos.filter(p => p.categoria === categoria);

  titulo.textContent = categoria === 'todos' ? 'Todos los productos' : NOMBRES_CATEGORIA[categoria];
  resumen.textContent = lista.length + (lista.length === 1 ? ' producto' : ' productos');

  grilla.innerHTML = lista.length
    ? lista.map(tarjetaProducto).join('')
    : '<div class="aviso-pendiente">Todavía no hay productos en esta categoría.</div>';

  botonesFiltro.forEach(b => b.classList.toggle('activo', b.dataset.categoria === categoria));
}

// Al hacer clic en un filtro se repinta y se actualiza la URL, para que al
// recargar la página se conserve la categoría elegida.
botonesFiltro.forEach(boton => {
  boton.addEventListener('click', () => {
    const categoria = boton.dataset.categoria;
    const url = categoria === 'todos'
      ? window.location.pathname
      : window.location.pathname + '?categoria=' + categoria;
    window.history.replaceState(null, '', url);
    pintar(categoria);
  });
});

/** Carga los productos del backend y pinta la categoría inicial. */
async function iniciar() {
  grilla.innerHTML = '<div class="aviso-pendiente">Cargando catálogo...</div>';
  try {
    productos = await cargarProductos();
  } catch (error) {
    grilla.innerHTML = '<div class="aviso-pendiente">No se pudo cargar el catálogo. Intenta de nuevo en unos minutos.</div>';
    return;
  }
  pintar(categoriaDesdeUrl());
}

iniciar();