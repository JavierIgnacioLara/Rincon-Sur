package com.example.proyectorinconsur.domain.model

interface ClasificacionRegistro {
    val accesoEntradaId: Int
    val generoId: Int
    val rangoEtarioId: Int
    val tipoRegistroId: Int
}

data class Estadisticas(
    val total: Int = 0,
    val transito: Int = 0,
    val compro: Int = 0,
    val hombres: Int = 0,
    val mujeres: Int = 0,
    val ninos: Int = 0,
    val adultos: Int = 0,
    val mayores: Int = 0
)
