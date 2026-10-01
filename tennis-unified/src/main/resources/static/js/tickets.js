/*
 * Programa:     tickets.js
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista tickets.html (Comprador US8, Generar PQRS).
 *               Permite crear un reporte de soporte (POST /tickets) y
 *               consultar los reportes propios de un comprador (GET /tickets/mios).
 */

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
 * Consulta y pinta en pantalla los tickets del comprador cuyo id está
 * escrito en el campo #compradorId. Si el campo está vacío, muestra un
 * mensaje de guía en vez de consultar el backend.
 */
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

// Envío del formulario para crear un nuevo ticket.
// Defecto conocido (ver Test Case TC-001, PQRS): igual que en el
// actualizar-stock.js original, solo se lee datos.error, por lo que un
// error del backend bajo otra llave (ej. "mensaje" o el nombre del campo)
// cae en el mensaje genérico de la línea de abajo. Pendiente aplicar aquí
// el mismo ajuste ya hecho en actualizar-stock.js.
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

// Al cambiar el id de comprador, recarga automáticamente su lista de reportes.
document.getElementById('compradorId').addEventListener('change', cargarMisTickets);