// Backend servido desde el mismo Spring Boot (src/main/resources/static),
// por eso la ruta es relativa.
const ENDPOINT_REGISTRO = "/auth/register";

const form = document.getElementById("form-registro");
const botonEnviar = document.getElementById("boton-enviar");
const botonTexto = document.getElementById("boton-texto");
const aviso = document.getElementById("aviso");

// El rol ya no se elige en un <select> — cada página (registro-comprador.html /
// registro-proveedor.html) lo fija de antemano con data-rol="comprador|proveedor"
// en la etiqueta <form>, para que un comprador nunca vea campos de proveedor y viceversa.
const rolFijo = form.dataset.rol;

const campoNombre = document.getElementById("nombre");
const campoCorreo = document.getElementById("correo");
const campoContrasena = document.getElementById("contrasena");
const campoConfirmar = document.getElementById("confirmar");

// Estos solo existen en registro-proveedor.html
const campoNombreEmpresa = document.getElementById("nombreEmpresa");
const campoNit = document.getElementById("nit");
const campoTelefono = document.getElementById("telefono");
const campoDireccion = document.getElementById("direccion");

const verPasswordBtn = document.getElementById("ver-password");

verPasswordBtn.addEventListener("click", () => {
  const esTexto = campoContrasena.type === "text";
  campoContrasena.type = esTexto ? "password" : "text";
  verPasswordBtn.textContent = esTexto ? "Ver" : "Ocultar";
});

function limpiarErrores() {
  document.querySelectorAll(".error").forEach(el => (el.textContent = ""));
  document.querySelectorAll("input").forEach(el => el.classList.remove("invalido"));
  aviso.hidden = true;
  aviso.className = "aviso";
}

function marcarError(campoId, mensaje) {
  const input = document.getElementById(campoId);
  const errorEl = document.getElementById("error-" + campoId);
  if (input) input.classList.add("invalido");
  if (errorEl) errorEl.textContent = mensaje;
}

function mostrarAviso(mensaje, tipo) {
  aviso.hidden = false;
  aviso.textContent = mensaje;
  aviso.className = "aviso " + tipo;
}

// Validación básica en el cliente (la validación real y definitiva vive en el backend,
// vía @Valid en RegistroRequest, y en AuthService para las reglas de proveedor)
function validarEnCliente() {
  let esValido = true;

  const nombre = campoNombre.value.trim();
  const correo = campoCorreo.value.trim();
  const contrasena = campoContrasena.value;
  const confirmar = campoConfirmar.value;

  if (nombre.length < 3) {
    marcarError("nombre", "Escribe tu nombre completo");
    esValido = false;
  }

  const correoRegex = /^[\w.+-]+@[\w-]+\.[a-zA-Z]{2,}$/;
  if (!correoRegex.test(correo)) {
    marcarError("correo", "Ingresa un correo válido");
    esValido = false;
  }

  if (contrasena.length < 8) {
    marcarError("contrasena", "Debe tener al menos 8 caracteres");
    esValido = false;
  }

  if (confirmar !== contrasena) {
    marcarError("confirmar", "Las contraseñas no coinciden");
    esValido = false;
  }

  if (rolFijo === "proveedor") {
    if (!campoNombreEmpresa.value.trim()) {
      marcarError("nombreEmpresa", "El nombre de la empresa es obligatorio");
      esValido = false;
    }
    if (!campoNit.value.trim()) {
      marcarError("nit", "El NIT es obligatorio");
      esValido = false;
    }
  }

  return esValido;
}

// El backend responde errores en dos formas distintas según el caso:
//  - { "mensaje": "..." }  -> viene de CorreoDuplicadoException, IllegalArgumentException, etc.
//  - { "nombreDeCampo": "mensaje de ese campo", ... }  -> viene de @Valid (MethodArgumentNotValidException)
function mostrarErrorBackend(datos) {
  if (datos && typeof datos.mensaje === "string") {
    mostrarAviso(datos.mensaje, "fallo");
    return;
  }
  if (datos && typeof datos === "object") {
    Object.entries(datos).forEach(([campo, mensaje]) => marcarError(campo, mensaje));
    mostrarAviso("Revisa los campos marcados.", "fallo");
    return;
  }
  mostrarAviso("No se pudo crear la cuenta.", "fallo");
}

form.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  limpiarErrores();

  if (!validarEnCliente()) {
    return;
  }

  const payload = {
    nombre: campoNombre.value.trim(),
    correo: campoCorreo.value.trim().toLowerCase(),
    contrasena: campoContrasena.value,
    rol: rolFijo
  };

  if (rolFijo === "proveedor") {
    payload.nombreEmpresa = campoNombreEmpresa.value.trim();
    payload.nit = campoNit.value.trim();
    payload.telefono = campoTelefono.value.trim();
    payload.direccion = campoDireccion.value.trim();
  }

  botonEnviar.disabled = true;
  botonTexto.textContent = "Creando cuenta...";

  try {
    const respuesta = await fetch(ENDPOINT_REGISTRO, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    const datos = await respuesta.json();

    if (respuesta.ok) {
      mostrarAviso("Cuenta creada correctamente. Tu id es: " + datos.id, "exito");
      form.reset();
    } else {
      mostrarErrorBackend(datos);
    }
  } catch (error) {
    mostrarAviso("No se pudo conectar con el servidor. Revisa tu conexión.", "fallo");
  } finally {
    botonEnviar.disabled = false;
    botonTexto.textContent = "Crear cuenta";
  }
});
