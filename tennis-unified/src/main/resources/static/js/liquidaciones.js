/*
 * Programa:     liquidaciones.js
 * Descripción:  Pantalla del administrador para liquidar a proveedores:
 *               generar (POST /admin/liquidaciones), listar, ver detalle
 *               y marcar como pagada.
 */

const msg = document.getElementById('mensaje');

function esc(t) {
  return String(t ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

function pesos(n) {
  return Number(n).toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });
}

async function cargarOpciones() {
  const [provs, motivos] = await Promise.all([
    fetch('/admin/liquidaciones/proveedores').then(r => r.json()),
    fetch('/admin/liquidaciones/motivos-salida').then(r => r.json())
  ]);
  document.getElementById('proveedor').innerHTML =
    '<option value="">Elige un proveedor</option>' + provs.map(p => `<option value="${p.id}">${esc(p.nombre)}</option>`).join('');
  document.getElementById('motivo').innerHTML =
    '<option value="">Motivo de salida</option>' + motivos.map(m => `<option value="${m.id}">${esc(m.nombre)}</option>`).join('');
}

async function cargarLiquidaciones() {
  const cont = document.getElementById('listaLiquidaciones');
  const lista = await fetch('/admin/liquidaciones').then(r => r.json());
  if (!lista.length) { cont.innerHTML = '<p>Aún no hay liquidaciones.</p>'; return; }
  cont.innerHTML = lista.map(l => `
    <div class="ticket">
      <span class="ticket-estado ${l.estado === 'pagada' ? 'cerrado' : 'abierto'}">${l.estado === 'pagada' ? 'Pagada' : 'Pendiente'}</span>
      <div class="ticket-asunto">${esc(l.proveedor)} — ${pesos(l.total)}</div>
      <div>${l.tipo === 'final' ? 'Final (' + esc(l.motivo) + ')' : 'Periódica'} · ${esc(l.fechaInicio)} a ${esc(l.fechaFin)}</div>
      <div class="ticket-acciones">
        <button onclick="verDetalle('${l.id}')">Ver detalle</button>
        ${l.estado === 'pendiente' ? `<button onclick="pagar('${l.id}')">Marcar como pagada</button>` : ''}
      </div>
      <div id="detalle-${l.id}"></div>
    </div>`).join('');
}

async function verDetalle(id) {
  const lineas = await fetch(`/admin/liquidaciones/${id}/detalle`).then(r => r.json());
  document.getElementById('detalle-' + id).innerHTML = lineas.length
    ? '<ul>' + lineas.map(x => `<li>Factura ${x.factura} · ${esc(x.producto)} ×${x.cantidad} → ${pesos(x.monto)}</li>`).join('') + '</ul>'
    : '<p>Sin ventas facturadas en este periodo.</p>';
}

async function pagar(id) {
  const r = await fetch(`/admin/liquidaciones/${id}/pagar`, { method: 'PUT' });
  const d = await r.json();
  msg.textContent = d.mensaje || 'No se pudo marcar como pagada.';
  cargarLiquidaciones();
}

document.getElementById('tipo').addEventListener('change', e => {
  document.getElementById('motivo').style.display = e.target.value === 'final' ? '' : 'none';
});

document.getElementById('formLiquidacion').addEventListener('submit', async (e) => {
  e.preventDefault();
  const tipo = document.getElementById('tipo').value;
  if (tipo === 'final' && !confirm('Una liquidación final desactiva al proveedor y todos sus productos. ¿Continuar?')) return;
  try {
    const r = await fetch('/admin/liquidaciones', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        proveedorId: document.getElementById('proveedor').value,
        fechaInicio: document.getElementById('desde').value,
        fechaFin: document.getElementById('hasta').value,
        tipo,
        motivoSalidaId: tipo === 'final' ? (document.getElementById('motivo').value || null) : null
      })
    });
    const d = await r.json();
    msg.textContent = d.mensaje || Object.values(d).join(' · ');
    if (r.ok) { cargarLiquidaciones(); if (tipo === 'final') cargarOpciones(); }
  } catch (err) {
    msg.textContent = 'No se pudo conectar con el servidor.';
  }
});

cargarOpciones();
cargarLiquidaciones();