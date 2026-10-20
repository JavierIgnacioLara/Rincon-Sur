package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.model.Turno
import com.example.proyectorinconsur.data.repository.ResultadoTurno
import com.example.proyectorinconsur.data.repository.TurnoRepository
import com.example.proyectorinconsur.data.repository.UsuarioRepository
import com.example.proyectorinconsur.data.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.proyectorinconsur.model.InicioUIState

@OptIn(ExperimentalCoroutinesApi::class)
class InicioViewModel(
    private val usuarios: UsuarioRepository,
    private val sesion: SessionManager,
    private val turnos: TurnoRepository,
    turnoActivo: Flow<Turno?>
) : ViewModel() {

    private val _estado = MutableStateFlow(InicioUIState())
    val estado: StateFlow<InicioUIState> = _estado

    val usuarioId: StateFlow<Long?> = sesion.usuarioId

    init {
        viewModelScope.launch {
            sesion.usuarioId
                .map { id -> id?.let { usuarios.buscarPorId(it)?.nombre }.orEmpty() }
                .collect { nombre -> _estado.update { it.copy(nombre = nombre) } }
        }
        viewModelScope.launch {
            turnoActivo.collect { turno -> _estado.update { it.copy(turnoActivo = turno) } }
        }
    }


    fun empezarTurno() {
        val s = _estado.value
        if (s.isSubmitting || s.turnoActivo != null) return
        val usuarioId = sesion.usuarioId.value ?: return
        _estado.update { it.copy(isSubmitting = true, errorMsg = null, mensaje = null) }

        viewModelScope.launch {
            val resultado = turnos.iniciarTurno(usuarioId, s.fotoPendiente)
            when (resultado) {
                is ResultadoTurno.Exito -> _estado.update {
                    it.copy(
                        isSubmitting = false,
                        turnoIniciado = true,
                        fotoPendiente = null
                    )
                }
                ResultadoTurno.YaHayTurnoActivo ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = "Ya tienes un turno activo.") }
                is ResultadoTurno.Error ->
                    _estado.update { it.copy(isSubmitting = false, errorMsg = resultado.mensaje) }
            }
        }
    }

    fun cerrarTurno() {
        val turno = _estado.value.turnoActivo ?: return
        if (_estado.value.isSubmitting) return
        _estado.update { it.copy(isSubmitting = true, errorMsg = null, mensaje = null) }
        viewModelScope.launch {
            val ok = turnos.cerrarTurno(turno.id)
            _estado.update {
                if (ok) it.copy(isSubmitting = false, mensaje = "Turno cerrado. Los registros quedaron guardados en el historial.")
                else it.copy(isSubmitting = false, errorMsg = "No se pudo cerrar el turno.")
            }
        }
    }

    fun consumirTurnoIniciado() = _estado.update { it.copy(turnoIniciado = false) }

    fun prepararCamara(uri: String) = _estado.update { it.copy(camaraUriPendiente = uri) }

    fun resultadoCamara(exito: Boolean) {
        val uri = _estado.value.camaraUriPendiente
        _estado.update { it.copy(camaraUriPendiente = null) }
        if (!exito || uri == null) {
            _estado.update { it.copy(mensaje = "Captura cancelada. Puedes continuar sin foto.") }
        } else {
            guardarFoto(uri)
        }
    }

    fun quitarFoto() = guardarFoto(null)

    private fun guardarFoto(uri: String?) {
        val turno = _estado.value.turnoActivo
        if (turno == null) {
            _estado.update { it.copy(fotoPendiente = uri, mensaje = if (uri != null) "Foto guardada." else null) }
            return
        }
        viewModelScope.launch {
            val ok = turnos.actualizarFoto(turno.id, uri)
            _estado.update {
                if (ok) it.copy(mensaje = if (uri != null) "Foto guardada en el turno." else null)
                else it.copy(errorMsg = "No se pudo guardar la foto.")
            }
        }
    }

    fun mostrarMensaje(texto: String) = _estado.update { it.copy(mensaje = texto, errorMsg = null) }
    fun mostrarError(texto: String) = _estado.update { it.copy(errorMsg = texto, mensaje = null) }
    fun consumirMensajes() = _estado.update { it.copy(mensaje = null, errorMsg = null) }

    fun cerrarSesion() {
        viewModelScope.launch { sesion.cerrar() }
    }
}