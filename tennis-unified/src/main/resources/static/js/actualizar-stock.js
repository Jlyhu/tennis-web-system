/*
 * Programa:     actualizar-stock.js
 * Versión:      1.1
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista actualizar-stock.html (Proveedor US5).
 *               Envía el nuevo stock de un producto al backend
 *               (PATCH /productos/{id}/stock) y muestra el resultado.
 * Cambios v1.1: se extrajo obtenerMensajeError() del manejador del formulario
 *               para poder probarla de forma aislada (PU-001). El
 *               comportamiento no cambia.
 */

/**
 * Elige el mensaje que se muestra cuando el backend responde con error.
 * El backend usa dos formatos: {"mensaje": "..."} para errores generales y
 * {"<campo>": "..."} para validaciones de un campo (ej. {"stock": "..."}),
 * por eso se toma el primer valor sin depender del nombre de la llave.
 * @param {Object} datos JSON de error devuelto por el backend
 * @returns {string} Mensaje para mostrar en pantalla
 */
function obtenerMensajeError(datos) {
  return datos.error || Object.values(datos)[0] || 'No se pudo actualizar el stock.';
}

document.getElementById('formStock').addEventListener('submit', async (e) => {
  e.preventDefault();
  const mensaje = document.getElementById('mensaje');
  mensaje.className = 'mensaje';

  const productoId = document.getElementById('productoId').value;
  const stock = parseInt(document.getElementById('stockNuevo').value, 10);

  try {
    const respuesta = await fetch(`/productos/${productoId}/stock`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ stock })
    });
    const datos = await respuesta.json();

    if (respuesta.ok) {
      mensaje.className = 'mensaje exito';
      mensaje.textContent = datos.mensaje;
    } else {
      mensaje.className = 'mensaje error';
      mensaje.textContent = obtenerMensajeError(datos);
    }
  } catch (err) {
    mensaje.className = 'mensaje error';
    mensaje.textContent = 'No se pudo conectar con el servidor.';
  }
});