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
      mensaje.textContent = datos.error || 'No se pudo actualizar el stock.';
    }
  } catch (err) {
    mensaje.className = 'mensaje error';
    mensaje.textContent = 'No se pudo conectar con el servidor.';
  }
});
