package com.example.proyectorinconsur.domain.model

data class Registro(
    val id: Long,
    val turnoId: Long,
    val fechaHora: Long,
    override val accesoEntradaId: Int,
    val accesoSalidaId: Int,
    override val generoId: Int,
    override val rangoEtarioId: Int,
    override val tipoRegistroId: Int
) : ClasificacionRegistro
