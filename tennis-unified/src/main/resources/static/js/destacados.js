/*
 * Programa:     destacados.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Llena la sección "Productos destacados" del index con
 *               productos reales del backend (GET /productos). Requiere
 *               cargar js/productos-ui.js antes que este archivo.
 */

/**
 * Elige productos repartidos entre las categorías: primero uno de cada
 * una, luego el segundo de cada una, y así hasta completar la cantidad.
 * @param {Array} lista productos con imagen
 * @param {number} cantidad cuántos productos elegir
 * @returns {Array} productos elegidos
 */
function elegirDestacados(lista, cantidad) {
  const porCategoria = {};
  lista.forEach(p => {
    (porCategoria[p.categoria] = porCategoria[p.categoria] || []).push(p);
  });
  const grupos = Object.values(porCategoria);
  const elegidos = [];
  for (let i = 0; elegidos.length < cantidad && i < lista.length; i++) {
    grupos.forEach(grupo => {
      if (grupo[i] && elegidos.length < cantidad) elegidos.push(grupo[i]);
    });
  }
  return elegidos;
}

/** Pide los productos y pinta los destacados en #destacados. */
async function pintarDestacados() {
  const contenedor = document.getElementById('destacados');
  try {
    const productos = await cargarProductos();
    const elegidos = elegirDestacados(productos.filter(p => p.imagenUrl), 4);
    contenedor.innerHTML = elegidos.length
      ? elegidos.map(tarjetaProducto).join('')
      : '<div class="aviso-pendiente">Todavía no hay productos publicados.</div>';
  } catch (error) {
    contenedor.innerHTML = '<div class="aviso-pendiente">No se pudieron cargar los productos en este momento.</div>';
  }
}

pintarDestacados();