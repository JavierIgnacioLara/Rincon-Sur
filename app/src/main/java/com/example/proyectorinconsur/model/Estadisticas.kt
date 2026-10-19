package com.example.proyectorinconsur.model

interface ClasificacionRegistro {
    val accesoEntradaId: Int
    val generoId: Int
    val rangoEtarioId: Int
    val tipoRegistroId: Int

    // Acompañantes de la persona registrada (opcionales; por defecto 0)
    val acompAdultos: Int get() = 0
    val acompNinos: Int get() = 0
}

data class Estadisticas(
    val total: Int = 0,
    val transito: Int = 0,
    val compro: Int = 0,
    val hombres: Int = 0,
    val mujeres: Int = 0,
    val ninos: Int = 0,
    val adultos: Int = 0,
    val mayores: Int = 0,
    // Acompañantes según el tipo de la persona que acompañan
    val acompTransitoAdultos: Int = 0,
    val acompTransitoNinos: Int = 0,
    val acompCompraAdultos: Int = 0,
    val acompCompraNinos: Int = 0
) {
    val acompTransito: Int get() = acompTransitoAdultos + acompTransitoNinos
    val acompCompra: Int get() = acompCompraAdultos + acompCompraNinos
}
