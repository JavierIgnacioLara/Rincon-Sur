package com.example.proyectorinconsur.data.repository

import com.example.proyectorinconsur.data.remote.ApiService
import com.example.proyectorinconsur.data.remote.ErrorDto
import com.example.proyectorinconsur.data.remote.LoginRequestDto
import com.example.proyectorinconsur.data.remote.UsuarioDto
import com.example.proyectorinconsur.data.remote.UsuarioRequestDto
import com.example.proyectorinconsur.model.Usuario
import com.google.gson.Gson
import java.io.IOException
import retrofit2.HttpException

sealed class ResultadoRegistro {
    data class Exito(val usuario: Usuario) : ResultadoRegistro()
    data object CorreoDuplicado : ResultadoRegistro()
    data class Error(val mensaje: String) : ResultadoRegistro()
}

sealed class ResultadoLogin {
    data class Exito(val usuario: Usuario) : ResultadoLogin()
    data object CredencialesInvalidas : ResultadoLogin()
    data class Error(val mensaje: String) : ResultadoLogin()
}

/** Resultado genérico para las operaciones del administrador (editar / eliminar operadores). */
sealed class ResultadoOperacion {
    data object Exito : ResultadoOperacion()
    data class Error(val mensaje: String) : ResultadoOperacion()
}

private fun UsuarioDto.toDomain() = Usuario(
    id = id, nombre = nombre, correo = correo, rol = rol, creadoEn = creadoEn
)

/** Saca el texto "mensaje" que manda el backend; si no hay, usa uno por defecto. */
internal fun HttpException.mensajeDelServidor(porDefecto: String): String =
    try {
        Gson().fromJson(response()?.errorBody()?.string(), ErrorDto::class.java)?.mensaje
            ?: porDefecto
    } catch (e: Exception) {
        porDefecto
    }

internal const val SIN_CONEXION = "No se pudo conectar con el servidor. Revisa que esté encendido."

open class UsuarioRepository(private val api: ApiService) {

    /**
     * Si no hay conexión se asume que SÍ hay usuarios (lo más seguro: así no se ofrece crear
     * una cuenta "primer usuario" por error cuando el servidor está apagado).
     */
    open suspend fun hayUsuarios(): Boolean =
        try { api.listarUsuarios().isNotEmpty() } catch (e: Exception) { true }

    /** El backend no tiene GET /usuarios/{id}; se busca en la lista. */
    open suspend fun buscarPorId(id: Long): Usuario? =
        try {
            api.listarUsuarios().firstOrNull { it.id == id }?.toDomain()
        } catch (e: Exception) {
            null
        }

    /** El backend decide el rol: el primer usuario es ADMIN, el resto OPERADOR. */
    open suspend fun registrar(nombre: String, correo: String, clave: String): ResultadoRegistro =
        crearConRol(nombre, correo, clave, null)

    open suspend fun crearConRol(
        nombre: String, correo: String, clave: String, rol: String?
    ): ResultadoRegistro =
        try {
            val dto = api.crearUsuario(
                UsuarioRequestDto(nombre.trim(), correo.trim().lowercase(), clave, rol)
            )
            ResultadoRegistro.Exito(dto.toDomain())
        } catch (e: HttpException) {
            if (e.code() == 409) ResultadoRegistro.CorreoDuplicado
            else ResultadoRegistro.Error(
                e.mensajeDelServidor("No se pudo crear la cuenta. Inténtalo nuevamente.")
            )
        } catch (e: IOException) {
            ResultadoRegistro.Error(SIN_CONEXION)
        } catch (e: Exception) {
            ResultadoRegistro.Error("No se pudo crear la cuenta. Inténtalo nuevamente.")
        }

    open suspend fun iniciarSesion(correo: String, clave: String): ResultadoLogin =
        try {
            val dto = api.login(LoginRequestDto(correo.trim().lowercase(), clave))
            ResultadoLogin.Exito(dto.toDomain())
        } catch (e: HttpException) {
            if (e.code() == 401) ResultadoLogin.CredencialesInvalidas
            else ResultadoLogin.Error("No se pudo iniciar sesión. Inténtalo nuevamente.")
        } catch (e: IOException) {
            ResultadoLogin.Error(SIN_CONEXION)
        } catch (e: Exception) {
            ResultadoLogin.Error("No se pudo iniciar sesión. Inténtalo nuevamente.")
        }

    // ---------- Administración de operadores (solo ADMIN en la UI) ----------

    open suspend fun listar(): List<Usuario> =
        try { api.listarUsuarios().map { it.toDomain() } } catch (e: Exception) { emptyList() }

    open suspend fun editar(
        id: Long, nombre: String, correo: String, clave: String?, rol: String
    ): ResultadoOperacion =
        try {
            api.editarUsuario(
                id,
                UsuarioRequestDto(nombre.trim(), correo.trim().lowercase(), clave?.takeIf { it.isNotBlank() }, rol)
            )
            ResultadoOperacion.Exito
        } catch (e: HttpException) {
            ResultadoOperacion.Error(e.mensajeDelServidor("No se pudo guardar los cambios."))
        } catch (e: IOException) {
            ResultadoOperacion.Error(SIN_CONEXION)
        }

    open suspend fun eliminar(id: Long): ResultadoOperacion =
        try {
            api.eliminarUsuario(id)
            ResultadoOperacion.Exito
        } catch (e: HttpException) {
            ResultadoOperacion.Error(e.mensajeDelServidor("No se pudo eliminar el usuario."))
        } catch (e: IOException) {
            ResultadoOperacion.Error(SIN_CONEXION)
        }
}
