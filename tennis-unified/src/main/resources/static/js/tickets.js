function textoEstado(estado) {
  return { abierto: 'Abierto', respondido: 'Respondido', cerrado: 'Cerrado' }[estado] || estado;
}

async function cargarMisTickets() {
  const compradorId = document.getElementById('compradorId').value;
  const lista = document.getElementById('listaTickets');
  if (!compradorId) {
    lista.innerHTML = '<p style="color:var(--texto-suave);font-size:0.9rem;">Escribe tu id de comprador arriba para ver tus reportes.</p>';
    return;
  }
  try {
    const respuesta = await fetch(`/tickets/mios?compradorId=${compradorId}`);
    const tickets = await respuesta.json();
    lista.innerHTML = '';
    if (tickets.length === 0) {
      lista.innerHTML = '<p style="color:var(--texto-suave);font-size:0.9rem;">Todavía no tienes reportes.</p>';
      return;
    }
    tickets.forEach(t => {
      const div = document.createElement('div');
      div.className = 'ticket';
      div.innerHTML = `
        <span class="ticket-estado ${t.estado}">${textoEstado(t.estado)}</span>
        <div class="ticket-asunto">${t.asunto}</div>
        <div>${t.descripcion}</div>
        ${t.respuesta ? `<div class="ticket-respuesta"><strong>Respuesta:</strong> ${t.respuesta}</div>` : ''}
      `;
      lista.appendChild(div);
    });
  } catch (err) {
    lista.innerHTML = '<p style="color:var(--error);font-size:0.9rem;">No se pudo conectar con el servidor.</p>';
  }
}

document.getElementById('formTicket').addEventListener('submit', async (e) => {
  e.preventDefault();
  const mensaje = document.getElementById('mensaje');
  mensaje.className = 'mensaje';

  const cuerpo = {
    compradorId: document.getElementById('compradorId').value,
    asunto: document.getElementById('asunto').value,
    descripcion: document.getElementById('descripcion').value
  };

  try {
    const respuesta = await fetch('/tickets', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cuerpo)
    });
    const datos = await respuesta.json();

    if (respuesta.ok) {
      mensaje.className = 'mensaje exito';
      mensaje.textContent = datos.mensaje;
      document.getElementById('asunto').value = '';
      document.getElementById('descripcion').value = '';
      cargarMisTickets();
    } else {
      mensaje.className = 'mensaje error';
      mensaje.textContent = datos.error || 'No se pudo enviar el reporte.';
    }
  } catch (err) {
    mensaje.className = 'mensaje error';
    mensaje.textContent = 'No se pudo conectar con el servidor.';
  }
});

document.getElementById('compradorId').addEventListener('change', cargarMisTickets);
