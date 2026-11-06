/*
 * Programa:     inventario.js
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista inventario.html (Proveedor US1, Registrar
 *               producto). Toma el id del proveedor de la sesión, así que ya
 *               no se escribe a mano, y envía el producto a POST /productos.
 *               La categoría se elige de una lista (raquetas, calzado o
 *               accesorios) para que el producto aparezca en los filtros del
 *               catálogo. Si no hay sesión de proveedor, lleva al login.
 *               Requiere cargar antes js/sesion.js.
 */

const sesionProveedor = obtenerSesion();
if (!sesionProveedor || sesionProveedor.rol !== 'proveedor' || !sesionProveedor.proveedorId) {
  window.location.href = 'login.html?volver=inventario.html';
}

/**
 * Saca un texto de error de la respuesta del backend. Acepta {"mensaje"},
 * {"error"} o un mapa campo -> mensaje (errores de validación).
 * @param {Object} datos cuerpo JSON de la respuesta
 * @param {string} porDefecto texto si no se encuentra ninguno
 * @returns {string} mensaje para el usuario
 */
function textoDeError(datos, porDefecto) {
  if (datos && typeof datos.mensaje === 'string') return datos.mensaje;
  if (datos && typeof datos.error === 'string') return datos.error;
  const primero = datos ? Object.values(datos).find(v => typeof v === 'string') : null;
  return primero || porDefecto;
}

document.getElementById('formProducto').addEventListener('submit', async (e) => {
  e.preventDefault();
  const mensaje = document.getElementById('mensaje');
  mensaje.className = 'mensaje';

  const cuerpo = {
    proveedorId: sesionProveedor.proveedorId,
    nombre: document.getElementById('nombre').value,
    descripcion: document.getElementById('descripcion').value,
    categoria: document.getElementById('categoria').value,
    precio: parseFloat(document.getElementById('precio').value),
    stock: parseInt(document.getElementById('stock').value, 10)
  };

  try {
    const respuesta = await fetch('/productos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cuerpo)
    });
    const datos = await respuesta.json().catch(() => ({}));

    if (respuesta.ok) {
      mensaje.className = 'mensaje exito';
      mensaje.textContent = 'Producto registrado. Ya aparece en el catálogo. Id: ' + datos.id;
      e.target.reset();
    } else {
      mensaje.className = 'mensaje error';
      mensaje.textContent = textoDeError(datos, 'No se pudo registrar el producto.');
    }
  } catch (err) {
    mensaje.className = 'mensaje error';
    mensaje.textContent = 'No se pudo conectar con el servidor.';
  }
});