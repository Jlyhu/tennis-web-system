function textoEstado(estado) {
  return { abierto: 'Abierto', respondido: 'Respondido', cerrado: 'Cerrado' }[estado] || estado;
}

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

async function cerrar(id) {
  await fetch(`/admin/tickets/${id}/cerrar`, { method: 'PUT' });
  cargarTickets();
}

cargarTickets();
