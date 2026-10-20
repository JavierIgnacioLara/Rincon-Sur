package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.data.repository.ResultadoRegistro
import com.example.proyectorinconsur.data.repository.UsuarioRepository
import com.example.proyectorinconsur.validation.validateStrongPassword
import com.example.proyectorinconsur.validation.validateConfirm
import com.example.proyectorinconsur.validation.validateEmail
import com.example.proyectorinconsur.validation.validateNameLettersOnly
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.proyectorinconsur.model.UsuarioErrores
import com.example.proyectorinconsur.model.UsuarioUIState

class CrearUsuarioViewModel(
    private val repositorio: UsuarioRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado: StateFlow<UsuarioUIState> = _estado

    init {
        viewModelScope.launch {
            val hayUsuarios = repositorio.hayUsuarios()
            _estado.update { it.copy(esPrimerUsuario = !hayUsuarios) }
        }
    }

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null), errorMsg = null) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null), errorMsg = null) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null, confirmacion = null), errorMsg = null) }
    }

    fun onConfirmacionChange(valor: String) {
        _estado.update { it.copy(confirmacion = valor, errores = it.errores.copy(confirmacion = null), errorMsg = null) }
    }

    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = UsuarioErrores(
            nombre = validateNameLettersOnly(actual.nombre),
            correo = validateEmail(actual.correo),
            clave = validateStrongPassword(actual.clave),
            confirmacion = validateConfirm(actual.clave, actual.confirmacion)
        )
        _estado.update { it.copy(errores = errores) }
        return listOfNotNull(errores.nombre, errores.correo, errores.clave, errores.confirmacion).isEmpty()
    }

    fun crearCuenta() {
        if (_estado.value.isSubmitting) return      // evita doble pulsación
        if (!validarFormulario()) return
        val s = _estado.value

        viewModelScope.launch {
            _estado.update { it.copy(isSubmitting = true, errorMsg = null) }
            when (val resultado = repositorio.registrar(s.nombre, s.correo, s.clave)) {
                // Los campos se limpian SOLO después de guardar con éxito
                is ResultadoRegistro.Exito -> _estado.update { UsuarioUIState(cuentaCreada = true) }
                ResultadoRegistro.CorreoDuplicado -> _estado.update {
                    it.copy(
                        isSubmitting = false,
                        errores = it.errores.copy(correo = "Este correo ya está registrado")
                    )
                }
                is ResultadoRegistro.Error ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = resultado.mensaje) }
            }
        }
    }

    fun consumirCuentaCreada() {
        _estado.update { it.copy(cuentaCreada = false) }
    }
}
