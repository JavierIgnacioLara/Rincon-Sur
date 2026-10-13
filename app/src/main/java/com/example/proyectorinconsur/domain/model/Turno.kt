package com.example.proyectorinconsur.domain.model

const val ESTADO_ACTIVO = "ACTIVO"
const val ESTADO_CERRADO = "CERRADO"

data class Turno(
    val id: Long,
    val usuarioId: Long,
    val fecha: Long,
    val horaInicio: Long,
    val horaFin: Long?,
    val estado: String,
    val fotoUri: String?
)