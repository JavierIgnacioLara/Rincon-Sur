package com.example.proyectorinconsur.data.session

import com.example.proyectorinconsur.data.entity.UserPreferences
import com.example.proyectorinconsur.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class SessionManager(
    private val preferencias: UserPreferences,
    private val usuarios: UsuarioRepository
) {
    private val _usuarioId = MutableStateFlow<Long?>(null)
    val usuarioId: StateFlow<Long?> = _usuarioId


    suspend fun restaurar() {
        if (_usuarioId.value != null) return
        val recordado = preferencias.usuarioRecordado.first()
        if (recordado != null && usuarios.buscarPorId(recordado) != null) {
            _usuarioId.value = recordado
        } else if (recordado != null) {
            preferencias.olvidarUsuario()
        }
    }

    suspend fun iniciar(id: Long, recordar: Boolean) {
        _usuarioId.value = id
        if (recordar) preferencias.recordarUsuario(id) else preferencias.olvidarUsuario()
    }

    suspend fun cerrar() {
        _usuarioId.value = null
        preferencias.olvidarUsuario()
    }
}
