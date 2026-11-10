/*
 * Programa:     movimientos.js
 * Versión:      1.0
 * Fecha:        09/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista movimientos.html (Proveedor, inventario
 *               normalizado). Muestra el stock actual y el stock mínimo de
 *               cada producto del proveedor (GET /inventario/proveedor/{id}/stock),
 *               registra entradas, salidas y ajustes (POST /inventario/movimientos),
 *               permite cambiar el stock mínimo (PUT /inventario/productos/{id}/stock-minimo)
 *               y muestra el historial de un producto. Toma el proveedor de
 *               la sesión. Requiere cargar antes js/sesion.js.
 */

const sesionProv = obtenerSesion();
if (!sesionProv || sesionProv.rol !== 'proveedor' || !sesionProv.proveedorId) {
  window.location.href = 'login.html?volver=movimientos.html';
}

/**
 * Escapa texto para insertarlo de forma segura en HTML.
 * @param {string} texto texto original
 * @returns {string} texto escapado
 */
function esc(texto) {
  return String(texto ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
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

/**
 * Muestra un mensaje en un contenedor .mensaje.
 * @param {string} id id del contenedor
 * @param {string} texto mensaje
 * @param {boolean} exito true para estilo de éxito, false para error
 */
function avisar(id, texto, exito) {
  const el = document.getElementById(id);
  el.className = 'mensaje ' + (exito ? 'exito' : 'error');
  el.textContent = texto;
}

/** Carga el stock del proveedor, pinta la tabla y llena el selector de productos. */
async function cargarStock() {
  const cuerpo = document.getElementById('tablaStock');
  try {
    const respuesta = await fetch(`/inventario/proveedor/${sesionProv.proveedorId}/stock`);
    const datos = await respuesta.json();
    if (!respuesta.ok) {
      avisar('mensajeStock', textoDeError(datos, 'No se pudo cargar el stock.'), false);
      return;
    }

    if (datos.length === 0) {
      cuerpo.innerHTML = '<tr><td colspan="4">Aún no tienes productos. Registra uno en «Nuevo producto».</td></tr>';
      document.getElementById('producto').innerHTML = '';
      document.getElementById('alertaMinimos').className = 'mensaje';
      return;
    }

    cuerpo.innerHTML = datos.map(p => `
      <tr>
        <td>${esc(p.producto)}</td>
        <td><strong>${p.stockActual}</strong>${p.bajoMinimo ? ' <span class="ticket-estado abierto">Reponer</span>' : ''}</td>
        <td>${p.stockMinimo}</td>
        <td>
          <button type="button" class="btn-outline" onclick="verHistorial('${p.productoId}', '${esc(p.producto).replace(/'/g, '')}')">Ver historial</button>
          <button type="button" class="btn-outline" onclick="cambiarMinimo('${p.productoId}', ${p.stockMinimo})">Cambiar mínimo</button>
        </td>
      </tr>`).join('');

    const seleccionado = document.getElementById('producto').value;
    document.getElementById('producto').innerHTML = datos
      .map(p => `<option value="${p.productoId}">${esc(p.producto)} (stock ${p.stockActual})</option>`).join('');
    if (seleccionado) document.getElementById('producto').value = seleccionado;

    const bajos = datos.filter(p => p.bajoMinimo).length;
    const alerta = document.getElementById('alertaMinimos');
    alerta.className = bajos ? 'mensaje error' : 'mensaje';
    alerta.textContent = bajos ? `${bajos} producto(s) por debajo del stock mínimo.` : '';
  } catch (e) {
    avisar('mensajeStock', 'No se pudo conectar con el servidor.', false);
  }
}

/**
 * Muestra el historial de movimientos de un producto.
 * @param {string} productoId id del producto
 * @param {string} nombre nombre del producto, para el título
 */
async function verHistorial(productoId, nombre) {
  try {
    const respuesta = await fetch(`/inventario/productos/${productoId}/movimientos`);
    const datos = await respuesta.json();
    if (!respuesta.ok) {
      avisar('mensajeStock', textoDeError(datos, 'No se pudo cargar el historial.'), false);
      return;
    }
    document.getElementById('tituloHistorial').textContent = '— ' + nombre;
    document.getElementById('tablaHistorial').hidden = datos.length === 0;
    document.getElementById('vacioHistorial').hidden = datos.length > 0;
    document.getElementById('vacioHistorial').textContent = 'Este producto no tiene movimientos.';
    document.getElementById('cuerpoHistorial').innerHTML = datos.map(m => `
      <tr>
        <td>${new Date(m.fecha).toLocaleString('es-CO')}</td>
        <td>${esc(m.tipo)}</td>
        <td>${m.cantidad > 0 ? '+' : ''}${m.cantidad}</td>
        <td>${esc(m.motivo || '')}</td>
      </tr>`).join('');
  } catch (e) {
    avisar('mensajeStock', 'No se pudo conectar con el servidor.', false);
  }
}

/**
 * Pide el nuevo stock mínimo y lo guarda.
 * @param {string} productoId id del producto
 * @param {number} actual stock mínimo actual
 */
async function cambiarMinimo(productoId, actual) {
  const texto = prompt('Nuevo stock mínimo (entero, 0 o más):', actual);
  if (texto === null) return;
  const valor = parseInt(texto, 10);
  if (Number.isNaN(valor) || valor < 0) {
    avisar('mensajeStock', 'El stock mínimo debe ser un número entero de 0 o más.', false);
    return;
  }
  try {
    const respuesta = await fetch(`/inventario/productos/${productoId}/stock-minimo`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ stockMinimo: valor })
    });
    const datos = await respuesta.json().catch(() => ({}));
    avisar('mensajeStock', respuesta.ok ? 'Stock mínimo actualizado.' : textoDeError(datos, 'No se pudo actualizar.'), respuesta.ok);
    if (respuesta.ok) cargarStock();
  } catch (e) {
    avisar('mensajeStock', 'No se pudo conectar con el servidor.', false);
  }
}

document.getElementById('formMovimiento').addEventListener('submit', async (e) => {
  e.preventDefault();
  const productoId = document.getElementById('producto').value;
  const cuerpo = {
    productoId,
    tipo: document.getElementById('tipo').value,
    cantidad: parseInt(document.getElementById('cantidad').value, 10),
    motivo: document.getElementById('motivo').value.trim() || null
  };

  try {
    const respuesta = await fetch('/inventario/movimientos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cuerpo)
    });
    const datos = await respuesta.json().catch(() => ({}));
    if (respuesta.ok) {
      avisar('mensaje', 'Movimiento registrado.', true);
      e.target.reset();
      document.getElementById('producto').value = productoId;
      await cargarStock();
      const opcion = document.getElementById('producto').selectedOptions[0];
      if (opcion) verHistorial(productoId, opcion.textContent.replace(/ \(stock \d+\)$/, ''));
    } else {
      avisar('mensaje', textoDeError(datos, 'No se pudo registrar el movimiento.'), false);
    }
  } catch (err) {
    avisar('mensaje', 'No se pudo conectar con el servidor.', false);
  }
});

cargarStock();