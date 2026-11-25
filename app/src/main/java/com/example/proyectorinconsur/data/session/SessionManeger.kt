package com.example.proyectorinconsur.data.session

import com.example.proyectorinconsur.data.session.UserPreferences
import com.example.proyectorinconsur.data.repository.UsuarioRepository
import com.example.proyectorinconsur.model.ROL_ADMIN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class SessionManager(
    private val preferencias: UserPreferences,
    private val usuarios: UsuarioRepository
) {
    private val _usuarioId = MutableStateFlow<Long?>(null)
    val usuarioId: StateFlow<Long?> = _usuarioId

    /** True solo si el usuario con sesión iniciada tiene rol ADMIN. Las pantallas lo usan para mostrar u ocultar opciones. */
    private val _esAdmin = MutableStateFlow(false)
    val esAdmin: StateFlow<Boolean> = _esAdmin

    suspend fun restaurar() {
        if (_usuarioId.value != null) return
        val recordado = preferencias.usuarioRecordado.first() ?: return
        val usuario = usuarios.buscarPorId(recordado)
        if (usuario != null) {
            _usuarioId.value = recordado
            _esAdmin.value = usuario.rol == ROL_ADMIN
        } else {
            preferencias.olvidarUsuario()
        }
    }

    suspend fun iniciar(id: Long, recordar: Boolean, rol: String? = null) {
        _usuarioId.value = id
        _esAdmin.value = rol == ROL_ADMIN
        if (recordar) preferencias.recordarUsuario(id) else preferencias.olvidarUsuario()
    }

    suspend fun cerrar() {
        _usuarioId.value = null
        _esAdmin.value = false
        preferencias.olvidarUsuario()
    }
}
