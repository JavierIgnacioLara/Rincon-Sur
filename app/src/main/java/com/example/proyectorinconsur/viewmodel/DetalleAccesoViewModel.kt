package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.data.session.FiltroHistorial
import com.example.proyectorinconsur.model.Turno
import com.example.proyectorinconsur.data.repository.RegistroRepository
import com.example.proyectorinconsur.usecase.calcularEstadisticas
import com.example.proyectorinconsur.usecase.deAcceso
import com.example.proyectorinconsur.utils.inicioDelDia
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.example.proyectorinconsur.model.DetalleAccesoUIState

@OptIn(ExperimentalCoroutinesApi::class)
class DetalleAccesoViewModel(
    private val accesoId: Int,
    private val filtro: FiltroHistorial,
    registros: RegistroRepository,
    turnoActivo: Flow<Turno?>
) : ViewModel() {

    val estado: StateFlow<DetalleAccesoUIState> = turnoActivo
        .flatMapLatest { turno ->
            if (turno == null) {
                flowOf(DetalleAccesoUIState())
            } else {
                registros.observarPorTurno(turno.id).map { lista ->
                    DetalleAccesoUIState(true, calcularEstadisticas(lista.deAcceso(accesoId)))
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetalleAccesoUIState())

    fun prepararHistorial() {
        filtro.fecha.value = inicioDelDia(System.currentTimeMillis())
        filtro.acceso.value = accesoId
    }
}