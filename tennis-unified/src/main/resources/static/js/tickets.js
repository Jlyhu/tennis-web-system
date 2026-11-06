/*
 * Programa:     tickets.js
 * Versión:      2.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista tickets.html (Comprador US8, Generar PQRS).
 *               Permite crear un reporte de soporte (POST /tickets) y
 *               consultar los reportes propios (GET /tickets/mios).
 * Cambios v2.0: el id del comprador sale de la sesión (ya no se escribe a
 *               mano); si no hay sesión de comprador, lleva al login y vuelve
 *               aquí; los errores del backend se leen de "mensaje" o "error";
 *               los textos se escapan antes de pintarlos.
 *               Requiere cargar antes js/sesion.js y js/productos-ui.js.
 */

const sesionActual = obtenerSesion();
if (!sesionActual || sesionActual.rol !== 'comprador') {
  window.location.href = 'login.html?volver=tickets.html';
}

/**
 * Traduce el estado interno del ticket (abierto/respondido/cerrado) a un
 * texto legible para el comprador.
 * @param {string} estado Estado devuelto por el backend
 * @returns {string} Texto en español para mostrar en pantalla
 */
function textoEstado(estado) {
  return { abierto: 'Abierto', respondido: 'Respondido', cerrado: 'Cerrado' }[estado] || estado;
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

/** Consulta y pinta los tickets del comprador que inició sesión. */
async function cargarMisTickets() {
  const lista = document.getElementById('listaTickets');
  try {
    const respuesta = await fetch(`/tickets/mios?compradorId=${sesionActual.id}`);
    const tickets = await respuesta.json();
    lista.innerHTML = '';
    if (!respuesta.ok || tickets.length === 0) {
      lista.innerHTML = '<p style="color:var(--text-secondary);font-size:0.9rem;">Todavía no tienes reportes.</p>';
      return;
    }
    tickets.forEach(t => {
      const div = document.createElement('div');
      div.className = 'ticket';
      div.innerHTML = `
        <span class="ticket-estado ${escaparHtml(t.estado)}">${escaparHtml(textoEstado(t.estado))}</span>
        <div class="ticket-asunto">${escaparHtml(t.asunto)}</div>
        <div>${escaparHtml(t.descripcion)}</div>
        ${t.respuesta ? `<div class="ticket-respuesta"><strong>Respuesta:</strong> ${escaparHtml(t.respuesta)}</div>` : ''}
      `;
      lista.appendChild(div);
    });
  } catch (err) {
    lista.innerHTML = '<p style="color:var(--error);font-size:0.9rem;">No se pudo conectar con el servidor.</p>';
  }
}

// Envío del formulario para crear un nuevo ticket.
document.getElementById('formTicket').addEventListener('submit', async (e) => {
  e.preventDefault();
  const mensaje = document.getElementById('mensaje');
  mensaje.className = 'mensaje';

  const cuerpo = {
    compradorId: sesionActual.id,
    asunto: document.getElementById('asunto').value,
    descripcion: document.getElementById('descripcion').value
  };

  try {
    const respuesta = await fetch('/tickets', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cuerpo)
    });
    const datos = await respuesta.json().catch(() => ({}));

    if (respuesta.ok) {
      mensaje.className = 'mensaje exito';
      mensaje.textContent = datos.mensaje;
      document.getElementById('asunto').value = '';
      document.getElementById('descripcion').value = '';
      cargarMisTickets();
    } else {
      mensaje.className = 'mensaje error';
      mensaje.textContent = textoDeError(datos, 'No se pudo enviar el reporte.');
    }
  } catch (err) {
    mensaje.className = 'mensaje error';
    mensaje.textContent = 'No se pudo conectar con el servidor.';
  }
});

cargarMisTickets();