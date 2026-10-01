/*
 * Programa:     admin-tickets.js
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista admin-tickets.html (Administrador US10,
 *               Canales de comunicación). Permite listar todos los tickets
 *               (GET /admin/tickets), responderlos
 *               (PUT /admin/tickets/{id}/responder) y cerrarlos
 *               (PUT /admin/tickets/{id}/cerrar).
 */

/**
 * Traduce el estado interno del ticket (abierto/respondido/cerrado) a un
 * texto legible para el administrador. Si el estado no es reconocido, lo
 * devuelve tal cual.
 * @param {string} estado Estado devuelto por el backend
 * @returns {string} Texto en español para mostrar en pantalla
 */
function textoEstado(estado) {
  return { abierto: 'Abierto', respondido: 'Respondido', cerrado: 'Cerrado' }[estado] || estado;
}

/**
 * Consulta todos los tickets del sistema y los pinta en #listaTickets.
 * Si no hay tickets, muestra un mensaje informativo. Los tickets que no
 * están cerrados incluyen un campo de respuesta y los botones "Responder"
 * y "Cerrar ticket"; los cerrados solo se muestran. Ante un error de red
 * muestra "No se pudo conectar con el servidor."
 */
async function cargarTickets() {
  const contenedor = document.getElementById('listaTickets');
  try {
    const respuesta = await fetch('/admin/tickets');
    const tickets = await respuesta.json();

    if (tickets.length === 0) {
      contenedor.innerHTML = '<div class="tarjeta"><p>No hay tickets todavía.</p></div>';
      return;
    }

    contenedor.innerHTML = tickets.map(t => `
      <div class="ticket">
        <span class="ticket-estado ${t.estado}">${textoEstado(t.estado)}</span>
        <div class="ticket-asunto">${t.asunto}</div>
        <div>${t.descripcion}</div>
        ${t.respuesta ? `<div style="margin-top:8px;color:var(--texto-suave);font-size:0.9rem;"><strong>Respuesta:</strong> ${t.respuesta}</div>` : ''}
        ${t.estado !== 'cerrado' ? `
          <div class="ticket-form">
            <textarea id="respuesta-${t.id}" rows="1" placeholder="Escribe una respuesta..."></textarea>
            <button onclick="responder('${t.id}')">Responder</button>
          </div>
          <div class="ticket-acciones">
            <button onclick="cerrar('${t.id}')">Cerrar ticket</button>
          </div>
        ` : ''}
      </div>
    `).join('');
  } catch (err) {
    contenedor.innerHTML = '<div class="mensaje error">No se pudo conectar con el servidor.</div>';
  }
}

/**
 * Envía la respuesta del administrador a un ticket y recarga la lista.
 * Toma el texto del campo #respuesta-{id}; si está vacío o solo tiene
 * espacios, no hace nada.
 * Pendiente: no revisa si el backend respondió con error, por lo que un
 * fallo no se muestra en pantalla. Además, el backend permite responder
 * tickets cerrados y esto los reabre (ver TicketSoporteService.responder).
 * @param {string} id Id del ticket a responder
 */
async function responder(id) {
  const respuesta = document.getElementById(`respuesta-${id}`).value;
  if (!respuesta.trim()) return;

  await fetch(`/admin/tickets/${id}/responder`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ respuesta })
  });
  cargarTickets();
}

/**
 * Cierra un ticket y recarga la lista.
 * Pendiente: no revisa la respuesta del backend, y el backend permite
 * cerrar tickets que nunca fueron respondidos (ver TicketSoporteService.cerrar).
 * @param {string} id Id del ticket a cerrar
 */
async function cerrar(id) {
  await fetch(`/admin/tickets/${id}/cerrar`, { method: 'PUT' });
  cargarTickets();
}

// Carga inicial: al abrir la vista se listan todos los tickets.
cargarTickets();