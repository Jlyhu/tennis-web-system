/*
 * Programa:     sesion.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Manejo de la sesión del usuario en el navegador. Guarda
 *               id, nombre, rol y proveedorId al iniciar sesión, decide a
 *               qué panel va cada rol y ajusta el menú (nombre, enlaces
 *               del rol y botón de cerrar sesión).
 * Nota:         la sesión vive en localStorage y sirve para mostrar la
 *               interfaz correcta; no protege el backend.
 */

const CLAVE_SESION = 'fsteni58_sesion';

// Página a la que va cada rol al iniciar sesión.
const DESTINO_POR_ROL = {
  administrador: 'clientes.html',
  proveedor: 'inventario.html',
  comprador: 'index.html'
};

// Enlaces extra que se agregan al menú según el rol.
const ENLACES_POR_ROL = {
  administrador: [
    { texto: 'Clientes', href: 'clientes.html' },
    { texto: 'Soporte', href: 'admin-tickets.html' }
  ],
  proveedor: [
    { texto: 'Inventario', href: 'inventario.html' },
    { texto: 'Stock', href: 'actualizar-stock.html' }
  ],
  comprador: [
    { texto: 'Soporte', href: 'tickets.html' }
  ]
};

/**
 * Normaliza el nombre del rol que llega del backend.
 * Cualquier rol que empiece por "admin" se trata como "administrador".
 * @param {string} rol nombre del rol
 * @returns {string} "administrador", "proveedor" o "comprador"
 */
function rolBase(rol) {
  const r = String(rol || '').toLowerCase();
  return r.startsWith('admin') ? 'administrador' : r;
}

/**
 * Lee la sesión guardada.
 * @returns {Object|null} {id, nombre, rol, proveedorId} o null si no hay sesión
 */
function obtenerSesion() {
  try {
    return JSON.parse(localStorage.getItem(CLAVE_SESION));
  } catch (e) {
    return null;
  }
}

/**
 * Guarda la sesión con los datos que devuelve POST /auth/login.
 * @param {Object} datos respuesta del login (id, nombre, rol, proveedorId)
 */
function guardarSesion(datos) {
  localStorage.setItem(CLAVE_SESION, JSON.stringify({
    id: datos.id,
    nombre: datos.nombre,
    rol: rolBase(datos.rol),
    proveedorId: datos.proveedorId || null
  }));
}

/** Borra la sesión y vuelve al inicio. */
function cerrarSesion() {
  localStorage.removeItem(CLAVE_SESION);
  window.location.href = 'index.html';
}

/**
 * Devuelve la página a la que debe ir un rol al iniciar sesión.
 * @param {string} rol nombre del rol
 * @returns {string} nombre del archivo HTML de destino
 */
function destinoDeRol(rol) {
  return DESTINO_POR_ROL[rolBase(rol)] || 'index.html';
}

/**
 * Ajusta el menú si hay sesión: el botón de login muestra el nombre y
 * lleva al panel del rol, se agrega "Cerrar sesión" y se suman al menú
 * los enlaces del rol. Solo actúa en páginas con un enlace #btn-login.
 */
function pintarSesionEnMenu() {
  const sesion = obtenerSesion();
  const boton = document.getElementById('btn-login');
  if (!sesion || !boton) return;

  boton.textContent = sesion.nombre;
  boton.href = destinoDeRol(sesion.rol);

  const salir = document.createElement('a');
  salir.href = '#';
  salir.className = 'login-btn login-btn-salir';
  salir.textContent = 'Cerrar sesión';
  salir.addEventListener('click', (e) => {
    e.preventDefault();
    cerrarSesion();
  });
  boton.insertAdjacentElement('afterend', salir);

  const menu = document.querySelector('.nav-links');
  if (menu) {
    (ENLACES_POR_ROL[sesion.rol] || []).forEach(enlace => {
      const a = document.createElement('a');
      a.className = 'nav-link';
      a.href = enlace.href;
      a.textContent = enlace.texto;
      menu.appendChild(a);
    });
  }
}

document.addEventListener('DOMContentLoaded', pintarSesionEnMenu);