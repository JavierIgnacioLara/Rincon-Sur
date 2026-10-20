package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.data.session.FiltroHistorial
import com.example.proyectorinconsur.data.repository.RegistroRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.example.proyectorinconsur.model.HistorialUIState

@OptIn(ExperimentalCoroutinesApi::class)
class HistorialViewModel(
    private val filtro: FiltroHistorial,
    registros: RegistroRepository
) : ViewModel() {

    val estado: StateFlow<HistorialUIState> = combine(filtro.fecha, filtro.acceso) { fecha, acceso -> fecha to acceso }
        .flatMapLatest { (fecha, acceso) ->
            registros.observarPorFecha(fecha).map { lista -> HistorialUIState(fecha, acceso, lista) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HistorialUIState(filtro.fecha.value, filtro.acceso.value)
        )

    fun seleccionarFecha(inicioDelDiaLocal: Long) { filtro.fecha.value = inicioDelDiaLocal }
    fun seleccionarAcceso(accesoId: Int) { filtro.acceso.value = accesoId }
}
