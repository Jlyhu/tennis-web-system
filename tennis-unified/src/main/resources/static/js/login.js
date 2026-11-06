/*
 * Programa:     login.js
 * Versión:      1.1
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de la vista login.html (Comprador US2, inicio de
 *               sesión). Valida el formulario en el cliente, envía las
 *               credenciales a POST /auth/login, guarda la sesión
 *               (sesion.js) y lleva al usuario al panel de su rol.
 * Cambios v1.1: se guarda la sesión y se redirige según el rol; se
 *               eliminó el manejador duplicado del botón de ver contraseña
 *               (ya lo maneja el <script> de login.html). Requiere cargar
 *               js/sesion.js antes que este archivo.
 */

// Backend servido desde el mismo Spring Boot (src/main/resources/static),
// por eso la ruta es relativa.
const ENDPOINT_LOGIN = "/auth/login";

// Solo se acepta volver a una página .html del mismo sitio (evita redirigir a sitios externos).
const VOLVER_VALIDO = /^[\w-]+\.html(\?[\w=&%-]*)?$/;

const form = document.getElementById("form-login");
const botonEnviar = document.getElementById("boton-enviar");
const botonTexto = document.getElementById("boton-texto");
const aviso = document.getElementById("aviso");

const campoCorreo = document.getElementById("correo");
const campoContrasena = document.getElementById("contrasena");

/** Limpia los mensajes de error y el aviso general antes de validar de nuevo. */
function limpiarErrores() {
  document.querySelectorAll(".error").forEach(el => (el.textContent = ""));
  document.querySelectorAll("input").forEach(el => el.classList.remove("invalido"));
  aviso.hidden = true;
  aviso.className = "aviso";
}

/**
 * Marca un campo como inválido y muestra su mensaje de error específico.
 * @param {string} campoId id del input (sin el prefijo "error-")
 * @param {string} mensaje texto a mostrar bajo el campo
 */
function marcarError(campoId, mensaje) {
  const input = document.getElementById(campoId);
  const errorEl = document.getElementById("error-" + campoId);
  if (input) input.classList.add("invalido");
  if (errorEl) errorEl.textContent = mensaje;
}

/**
 * Muestra el aviso general (éxito o fallo) encima del botón de envío.
 * @param {string} mensaje texto a mostrar
 * @param {string} tipo "exito" o "fallo", controla el color del aviso
 */
function mostrarAviso(mensaje, tipo) {
  aviso.hidden = false;
  aviso.textContent = mensaje;
  aviso.className = "aviso " + tipo;
}

/**
 * Valida el formulario en el cliente antes de enviarlo. La validación real
 * y definitiva vive en el backend.
 * @returns {boolean} true si correo y contraseña tienen un formato aceptable
 */
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

/**
 * Elige el mensaje que se muestra cuando el backend responde con error.
 * El backend usa dos formatos: {"mensaje": "..."} para errores de
 * credenciales (401), y {"<campo>": "..."} para errores de validación de
 * un campo (@Valid).
 * @param {Object} datos JSON de error devuelto por el backend
 */
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

/**
 * Decide a qué página ir después de iniciar sesión. Un comprador vuelve a
 * la página de la que venía (parámetro ?volver=) si es válida; los demás
 * roles van a su panel.
 * @param {string} rol rol del usuario
 * @returns {string} nombre del archivo HTML de destino
 */
function elegirDestino(rol) {
  const volver = new URLSearchParams(window.location.search).get("volver");
  if (rolBase(rol) === "comprador" && volver && VOLVER_VALIDO.test(volver)) {
    return volver;
  }
  return destinoDeRol(rol);
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
      guardarSesion(datos);
      mostrarAviso("Bienvenido/a, " + datos.nombre + ". Entrando...", "exito");
      setTimeout(() => {
        window.location.href = elegirDestino(datos.rol);
      }, 700);
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