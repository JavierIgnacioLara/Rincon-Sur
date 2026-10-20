package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.data.repository.ResultadoLogin
import com.example.proyectorinconsur.data.repository.UsuarioRepository
import com.example.proyectorinconsur.data.session.SessionManager
import com.example.proyectorinconsur.validation.validateLoginPassword
import com.example.proyectorinconsur.validation.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.proyectorinconsur.model.LoginErrores
import com.example.proyectorinconsur.model.LoginUIState

class LoginViewModel(
    private val repositorio: UsuarioRepository,
    private val sesion: SessionManager
) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUIState())
    val estado: StateFlow<LoginUIState> = _estado

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null), errorMsg = null) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null), errorMsg = null) }
    }

    fun onRecordarChange(valor: Boolean) {
        _estado.update { it.copy(recordar = valor) }
    }

    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = LoginErrores(
            correo = validateEmail(actual.correo),
            clave = validateLoginPassword(actual.clave)
        )
        _estado.update { it.copy(errores = errores) }
        return errores.correo == null && errores.clave == null
    }

    fun iniciarSesion() {
        if (_estado.value.isSubmitting) return
        if (!validarFormulario()) return
        val s = _estado.value

        viewModelScope.launch {
            _estado.update { it.copy(isSubmitting = true, errorMsg = null) }
            when (val resultado = repositorio.iniciarSesion(s.correo, s.clave)) {
                is ResultadoLogin.Exito -> {
                    sesion.iniciar(resultado.usuario.id, s.recordar)
                    _estado.update { it.copy(isSubmitting = false, clave = "", inicioExitoso = true) }
                }
                ResultadoLogin.CredencialesInvalidas ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = "Correo o contraseña incorrectos") }
                is ResultadoLogin.Error ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = resultado.mensaje) }
            }
        }
    }

    fun consumirInicioExitoso() {
        _estado.update { it.copy(inicioExitoso = false) }
    }
}