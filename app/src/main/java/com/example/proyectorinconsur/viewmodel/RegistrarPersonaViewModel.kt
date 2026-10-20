package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.model.Turno
import com.example.proyectorinconsur.data.repository.RegistroRepository
import com.example.proyectorinconsur.data.repository.ResultadoRegistroPersona
import com.example.proyectorinconsur.validation.validateEntryAccess
import com.example.proyectorinconsur.validation.validateExitAccess
import com.example.proyectorinconsur.validation.validateGender
import com.example.proyectorinconsur.validation.validateAgeRange
import com.example.proyectorinconsur.validation.validateRecordType
import com.example.proyectorinconsur.validation.validateActiveShift
import com.example.proyectorinconsur.validation.validateCompanions
import com.example.proyectorinconsur.model.MAX_ACOMPANANTES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.proyectorinconsur.model.RegistrarPersonaErrores
import com.example.proyectorinconsur.model.RegistrarPersonaUIState

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrarPersonaViewModel(
    private val registros: RegistroRepository,
    turnoActivo: Flow<Turno?>
) : ViewModel() {

    private val _estado = MutableStateFlow(RegistrarPersonaUIState())
    val estado: StateFlow<RegistrarPersonaUIState> = _estado

    private var jobMensajeExito: Job? = null

    init {
        viewModelScope.launch {
            turnoActivo.collect { turno -> _estado.update { it.copy(turnoActivo = turno) } }
        }
    }

    fun onEntrada(id: Int) = _estado.update { it.copy(entradaId = id, errores = it.errores.copy(entrada = null), errorMsg = null) }
    fun onGenero(id: Int) = _estado.update { it.copy(generoId = id, errores = it.errores.copy(genero = null), errorMsg = null) }
    fun onRango(id: Int) = _estado.update { it.copy(rangoId = id, errores = it.errores.copy(rango = null), errorMsg = null) }
    fun onTipo(id: Int) = _estado.update { it.copy(tipoId = id, errores = it.errores.copy(tipo = null), errorMsg = null) }
    fun onSalida(id: Int) = _estado.update { it.copy(salidaId = id, errores = it.errores.copy(salida = null), errorMsg = null) }

    fun onAcompAdultos(valor: Int) = _estado.update {
        it.copy(acompAdultos = valor.coerceIn(0, MAX_ACOMPANANTES), errores = it.errores.copy(acompanantes = null), errorMsg = null)
    }
    fun onAcompNinos(valor: Int) = _estado.update {
        it.copy(acompNinos = valor.coerceIn(0, MAX_ACOMPANANTES), errores = it.errores.copy(acompanantes = null), errorMsg = null)
    }

    fun validarFormulario(): Boolean {
        val s = _estado.value
        val errores = RegistrarPersonaErrores(
            entrada = validateEntryAccess(s.entradaId),
            genero = validateGender(s.generoId),
            rango = validateAgeRange(s.rangoId),
            tipo = validateRecordType(s.tipoId),
            salida = validateExitAccess(s.salidaId),
            acompanantes = validateCompanions(s.acompAdultos, s.acompNinos)
        )
        val errorTurno = validateActiveShift(s.turnoActivo != null)
        val mensaje = errorTurno ?: if (!errores.sinErrores) "Completa los campos marcados en rojo" else null
        _estado.update { it.copy(errores = errores, errorMsg = mensaje) }
        return errorTurno == null && errores.sinErrores
    }

    fun registrar() {
        if (_estado.value.isSubmitting) return
        if (!validarFormulario()) return
        val s = _estado.value
        val turno = s.turnoActivo ?: return
        val entrada = s.entradaId ?: return
        val salida = s.salidaId ?: return
        val genero = s.generoId ?: return
        val rango = s.rangoId ?: return
        val tipo = s.tipoId ?: return

        _estado.update { it.copy(isSubmitting = true, errorMsg = null) }
        viewModelScope.launch {
            val resultado = registros.registrar(turno.id, entrada, salida, genero, rango, tipo, s.acompAdultos, s.acompNinos)
            when (resultado) {
                ResultadoRegistroPersona.Exito -> {
                    _estado.update {
                        it.copy(
                            isSubmitting = false,
                            entradaId = null, generoId = null, rangoId = null, tipoId = null, salidaId = null,
                            acompAdultos = 0, acompNinos = 0,
                            errores = RegistrarPersonaErrores(),
                            registroExitoso = true
                        )
                    }
                    ocultarExitoLuego()
                }
                ResultadoRegistroPersona.SinTurnoActivo ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = "El turno ya no está activo. Inicia un turno nuevo.") }
                is ResultadoRegistroPersona.Error ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = resultado.mensaje) }
            }
        }
    }

    private fun ocultarExitoLuego() {
        jobMensajeExito?.cancel()
        jobMensajeExito = viewModelScope.launch {
            delay(2000)
            _estado.update { it.copy(registroExitoso = false) }
        }
    }
}