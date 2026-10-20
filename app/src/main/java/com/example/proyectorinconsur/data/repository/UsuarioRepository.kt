package com.example.proyectorinconsur.data.repository

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.proyectorinconsur.model.ROL_ADMIN
import com.example.proyectorinconsur.model.ROL_OPERADOR
import com.example.proyectorinconsur.model.Usuario
import com.example.proyectorinconsur.data.dao.UsuarioDao
import com.example.proyectorinconsur.data.entity.UsuarioEntity
import com.example.proyectorinconsur.data.entity.toDomain

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

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun hayUsuarios(): Boolean = dao.contar() > 0

    suspend fun buscarPorId(id: Long): Usuario? = dao.obtenerPorId(id)?.toDomain()

    suspend fun registrar(nombre: String, correo: String, clave: String): ResultadoRegistro =
        withContext(Dispatchers.Default) {   // el hash es costoso: fuera del hilo principal
            try {
                val correoNormalizado = correo.trim().lowercase()
                if (dao.obtenerPorCorreo(correoNormalizado) != null) {
                    return@withContext ResultadoRegistro.CorreoDuplicado
                }
                val rol = if (dao.contar() == 0) ROL_ADMIN else ROL_OPERADOR
                val entidad = UsuarioEntity(
                    nombre = nombre.trim(),
                    correo = correoNormalizado,
                    clave = clave,
                    rol = rol,
                    creadoEn = System.currentTimeMillis()
                )
                val id = dao.insertar(entidad)
                ResultadoRegistro.Exito(entidad.copy(id = id).toDomain())
            } catch (e: SQLiteConstraintException) {
                ResultadoRegistro.CorreoDuplicado
            } catch (e: Exception) {
                ResultadoRegistro.Error("No se pudo crear la cuenta. Inténtalo nuevamente.")
            }
        }

    suspend fun iniciarSesion(correo: String, clave: String): ResultadoLogin =
        withContext(Dispatchers.Default) {
            try {
                val entidad = dao.obtenerPorCorreo(correo.trim().lowercase())
                if (entidad != null && entidad.clave == clave) {
                    ResultadoLogin.Exito(entidad.toDomain())
                } else {
                    ResultadoLogin.CredencialesInvalidas
                }
            } catch (e: Exception) {
                ResultadoLogin.Error("No se pudo iniciar sesión. Inténtalo nuevamente.")
            }
        }
}
