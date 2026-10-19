package com.example.proyectorinconsur.model


data class Registro(
    val id: Long,
    val turnoId: Long,
    val fechaHora: Long,
    override val accesoEntradaId: Int,   // 1..4
    val accesoSalidaId: Int,             // 1..4
    override val generoId: Int,
    override val rangoEtarioId: Int,
    override val tipoRegistroId: Int,    // transitó o compró
    override val acompAdultos: Int = 0,  // acompañantes adultos (opcional)
    override val acompNinos: Int = 0     // acompañantes niños (opcional)
) : ClasificacionRegistro
