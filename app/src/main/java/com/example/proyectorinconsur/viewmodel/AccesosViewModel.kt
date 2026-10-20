package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.model.Turno
import com.example.proyectorinconsur.data.repository.RegistroRepository
import com.example.proyectorinconsur.model.ACCESOS
import com.example.proyectorinconsur.usecase.calcularEstadisticas
import com.example.proyectorinconsur.usecase.deAcceso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.example.proyectorinconsur.model.AccesosUIState

@OptIn(ExperimentalCoroutinesApi::class)
class AccesosViewModel(
    registros: RegistroRepository,
    turnoActivo: Flow<Turno?>
) : ViewModel() {

    val estado: StateFlow<AccesosUIState> = turnoActivo
        .flatMapLatest { turno ->
            if (turno == null) {
                flowOf(AccesosUIState())
            } else {
                registros.observarPorTurno(turno.id).map { lista ->
                    AccesosUIState(
                        hayTurnoActivo = true,
                        resumen = calcularEstadisticas(lista),
                        porAcceso = ACCESOS.associate { it.id to calcularEstadisticas(lista.deAcceso(it.id)) }
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AccesosUIState())
}