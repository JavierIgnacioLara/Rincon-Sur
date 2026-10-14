package com.example.proyectorinconsur.domain.model

data class DetalleAccesoUIState(
    val hayTurnoActivo: Boolean = false,
    val estadisticas: Estadisticas = Estadisticas()
)