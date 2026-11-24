package com.example.proyectorinconsur.data.remote

data class TurnoRequestDto(val usuarioId: Long, val foto: String?)

data class FotoRequestDto(val foto: String)

data class TurnoDto(
    val id: Long,
    val usuarioId: Long,
    val horaInicio: Long,
    val horaFin: Long?,
    val estado: String,
    val foto: String?
)

data class RegistroRequestDto(
    val turnoId: Long,
    val fechaHora: Long?,
    val accesoEntradaId: Int,
    val accesoSalidaId: Int,
    val generoId: Int,
    val rangoEtarioId: Int,
    val tipoRegistroId: Int,
    val acompAdultos: Int,
    val acompNinos: Int
)

data class RegistroDto(
    val id: Long,
    val turnoId: Long,
    val fechaHora: Long,
    val accesoEntradaId: Int,
    val accesoSalidaId: Int,
    val generoId: Int,
    val rangoEtarioId: Int,
    val tipoRegistroId: Int,
    val acompAdultos: Int,
    val acompNinos: Int
)
