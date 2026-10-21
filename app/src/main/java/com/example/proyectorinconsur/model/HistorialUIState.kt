package com.example.proyectorinconsur.model

import com.example.proyectorinconsur.usecase.calcularEstadisticas
import com.example.proyectorinconsur.usecase.deAcceso


data class HistorialUIState (
    val fechaMillis: Long = 0L,
    val accesoSeleccionado: Int = 0,
    val registrosDelDia: List<Registro> = emptyList()
){
    val resumen: Estadisticas get() = calcularEstadisticas(registrosDelDia)

    val registrosDelAcceso: List<Registro> get() = registrosDelDia.deAcceso(accesoSeleccionado)
}