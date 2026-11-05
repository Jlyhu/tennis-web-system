/*
 * Programa:     catalogo.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista catalogo.html. Pinta los productos del
 *               catálogo y permite filtrarlos por categoría. La categoría
 *               inicial se toma del parámetro ?categoria= de la URL
 *               (raquetas, calzado o accesorios), así los enlaces del
 *               index llegan ya filtrados.
 * Pendiente:    los productos están escritos aquí a mano (datos de
 *               ejemplo). Falta conectarlos al backend: un GET /productos
 *               y una columna de imagen en la tabla productos.
 */

// Datos de ejemplo. Los precios son ilustrativos, en pesos colombianos (COP).
const PRODUCTOS = [
  // --- Raquetas ---
  { nombre: 'Raqueta Grafito Pro', categoria: 'raquetas', precio: 489900,
    descripcion: 'Marco de grafito para jugadores de nivel avanzado.',
    imagen: 'imagenes/productos/raqueta-1-grafito.jpg' },
  { nombre: 'Raqueta Aluminio Iniciación', categoria: 'raquetas', precio: 159900,
    descripcion: 'Ligera y resistente, ideal para empezar a jugar.',
    imagen: 'imagenes/productos/raqueta-2-aluminio.jpg' },
  { nombre: 'Raqueta Silicio Control', categoria: 'raquetas', precio: 329900,
    descripcion: 'Mayor control y estabilidad en cada golpe.',
    imagen: 'imagenes/productos/raqueta-3-silicio.jpg' },
  { nombre: 'Raqueta Junior', categoria: 'raquetas', precio: 119900,
    descripcion: 'Tamaño reducido para jugadores jóvenes.',
    imagen: 'imagenes/productos/raqueta-4-junior.jpg' },
  { nombre: 'Raqueta Potencia', categoria: 'raquetas', precio: 379900,
    descripcion: 'Cabeza amplia para generar más potencia.',
    imagen: 'imagenes/productos/raqueta-5-potencia.jpg' },
  { nombre: 'Raqueta Torneo', categoria: 'raquetas', precio: 429900,
    descripcion: 'Peso equilibrado para partidos largos de competencia.',
    imagen: 'imagenes/productos/raqueta-6-torneo.jpg' },

  // --- Calzado ---
  { nombre: 'Zapatillas Arcilla', categoria: 'calzado', precio: 259900,
    descripcion: 'Suela con agarre para canchas de polvo de ladrillo.',
    imagen: 'imagenes/productos/zapatillas-1-arcilla.jpg' },
  { nombre: 'Zapatillas Cancha Dura', categoria: 'calzado', precio: 289900,
    descripcion: 'Más durabilidad y amortiguación en superficie dura.',
    imagen: 'imagenes/productos/zapatillas-2-dura.jpg' },
  { nombre: 'Zapatillas Todo Terreno', categoria: 'calzado', precio: 239900,
    descripcion: 'Versátiles para jugar en cualquier superficie.',
    imagen: 'imagenes/productos/zapatillas-3-todoterreno.jpg' },
  { nombre: 'Zapatillas Ligeras', categoria: 'calzado', precio: 269900,
    descripcion: 'Diseño liviano para desplazamientos rápidos.',
    imagen: 'imagenes/productos/zapatillas-4-ligeras.jpg' },
  { nombre: 'Zapatillas Estabilidad', categoria: 'calzado', precio: 279900,
    descripcion: 'Soporte lateral reforzado para los cambios de dirección.',
    imagen: 'imagenes/productos/zapatillas-5-estabilidad.jpg' },
  { nombre: 'Zapatillas Juveniles', categoria: 'calzado', precio: 199900,
    descripcion: 'Comodidad y resistencia para entrenar a diario.',
    imagen: 'imagenes/productos/zapatillas-6-juvenil.jpg' },

  // --- Accesorios y pelotas ---
  { nombre: 'Pelotas de Tenis (tubo x3)', categoria: 'accesorios', precio: 24900,
    descripcion: 'Presión sostenida para entrenamiento y partido.',
    imagen: 'imagenes/productos/accesorios-1-pelotas.jpg' },
  { nombre: 'Overgrips (pack x3)', categoria: 'accesorios', precio: 29900,
    descripcion: 'Mejoran el agarre y absorben el sudor.',
    imagen: 'imagenes/productos/accesorios-2-overgrips.jpg' },
  { nombre: 'Anti-vibradores (pack x2)', categoria: 'accesorios', precio: 14900,
    descripcion: 'Reducen la vibración del encordado.',
    imagen: 'imagenes/productos/accesorios-3-antivibradores.jpg' },
  { nombre: 'Estuche para Raqueta', categoria: 'accesorios', precio: 89900,
    descripcion: 'Protege tu raqueta durante el transporte.',
    imagen: 'imagenes/productos/accesorios-4-estuche.jpg' },
  { nombre: 'Maletero Deportivo', categoria: 'accesorios', precio: 219900,
    descripcion: 'Espacio para raquetas, calzado y ropa.',
    imagen: 'imagenes/productos/accesorios-5-maletero.jpg' },
  { nombre: 'Cuerda para Raqueta', categoria: 'accesorios', precio: 69900,
    descripcion: 'Cuerda multifilamento para mayor confort.',
    imagen: 'imagenes/productos/accesorios-6-cuerdas.jpg' }
];

// Nombre legible de cada categoría; también define cuáles son válidas.
const NOMBRES_CATEGORIA = {
  raquetas: 'Raquetas Premium',
  calzado: 'Zapatillas & Calzado',
  accesorios: 'Accesorios & Pelotas'
};

const grilla = document.getElementById('grilla');
const titulo = document.getElementById('catalogo-titulo');
const resumen = document.getElementById('resumen');
const botonesFiltro = document.querySelectorAll('.filtro');

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
 * Da formato de pesos colombianos a un número.
 * @param {number} valor precio sin formato
 * @returns {string} por ejemplo "$489.900 COP"
 */
function formatearPrecio(valor) {
  return '$' + valor.toLocaleString('es-CO') + ' COP';
}

/**
 * Pinta en pantalla los productos de una categoría y marca el filtro activo.
 * @param {string} categoria "todos" o una llave de NOMBRES_CATEGORIA
 */
function pintar(categoria) {
  const lista = categoria === 'todos'
    ? PRODUCTOS
    : PRODUCTOS.filter(p => p.categoria === categoria);

  titulo.textContent = categoria === 'todos' ? 'Todos los productos' : NOMBRES_CATEGORIA[categoria];
  resumen.textContent = lista.length + ' productos';

  grilla.innerHTML = lista.map(p => `
    <article class="producto">
      <div class="producto-imagen">
        <img src="${p.imagen}" alt="${p.nombre}"
             onerror="this.parentElement.classList.add('sin-imagen')">
      </div>
      <div class="producto-info">
        <span class="producto-categoria">${NOMBRES_CATEGORIA[p.categoria]}</span>
        <h3 class="producto-nombre">${p.nombre}</h3>
        <p class="producto-descripcion">${p.descripcion}</p>
        <span class="producto-precio">${formatearPrecio(p.precio)}</span>
      </div>
    </article>
  `).join('');

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

// Carga inicial: toma la categoría de la URL (o "todos").
pintar(categoriaDesdeUrl());