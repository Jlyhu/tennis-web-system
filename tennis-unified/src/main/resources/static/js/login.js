// Backend servido desde el mismo Spring Boot (src/main/resources/static),
// por eso la ruta es relativa.
const ENDPOINT_LOGIN = "/auth/login";

const form = document.getElementById("form-login");
const botonEnviar = document.getElementById("boton-enviar");
const botonTexto = document.getElementById("boton-texto");
const aviso = document.getElementById("aviso");

const campoCorreo = document.getElementById("correo");
const campoContrasena = document.getElementById("contrasena");

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

// Validación básica en el cliente (la validación real vive en el backend)
function validarEnCliente() {
  let esValido = true;

  const correo = campoCorreo.value.trim();
  const contrasena = campoContrasena.value;

  const correoRegex = /^[\w.+-]+@[\w-]+\.[a-zA-Z]{2,}$/;
  if (!correoRegex.test(correo)) {
    marcarError("correo", "Ingresa un correo válido");
    esValido = false;
  }

  if (contrasena.length < 1) {
    marcarError("contrasena", "Ingresa tu contraseña");
    esValido = false;
  }

  return esValido;
}

// El backend responde errores en dos formas distintas según el caso:
//  - { "mensaje": "..." }  -> viene de CredencialesInvalidasException (401), etc.
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
  mostrarAviso("No se pudo iniciar sesión.", "fallo");
}

form.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  limpiarErrores();

  if (!validarEnCliente()) {
    return;
  }

  const payload = {
    correo: campoCorreo.value.trim().toLowerCase(),
    contrasena: campoContrasena.value
  };

  botonEnviar.disabled = true;
  botonTexto.textContent = "Entrando...";

  try {
    const respuesta = await fetch(ENDPOINT_LOGIN, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    const datos = await respuesta.json();

    if (respuesta.ok) {
      mostrarAviso("Bienvenido/a, " + datos.nombre, "exito");
    } else {
      mostrarErrorBackend(datos);
    }
  } catch (error) {
    mostrarAviso("No se pudo conectar con el servidor. Revisa tu conexión.", "fallo");
  } finally {
    botonEnviar.disabled = false;
    botonTexto.textContent = "Entrar";
  }
});
