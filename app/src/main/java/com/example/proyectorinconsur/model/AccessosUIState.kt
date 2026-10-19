package com.example.proyectorinconsur.model

data class AccessosUIState(
    val hayTurnoActivo: Boolean = false,
    val resumen: Estadisticas = Estadisticas(),
    val porAcceso: Map<Int, Estadisticas> = emptyMap()
){
    fun de(acceso: Int): Estadisticas = porAcceso[acceso] ?: Estadisticas()
}