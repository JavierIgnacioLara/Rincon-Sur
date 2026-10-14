package com.example.proyectorinconsur.domain.model


data class Opcion(val id: Int, val etiqueta: String, val detalle: String? = null)

const val GENERO_HOMBRE = 1
const val GENERO_MUJER = 2

const val RANGO_NINO = 1
const val RANGO_ADULTO = 2
const val RANGO_MAYOR = 3

const val TIPO_TRANSITO = 1
const val TIPO_COMPRO = 2

val ACCESOS = (1..4).map { Opcion(it, "Acceso $it") }

val GENEROS = listOf(
    Opcion(GENERO_HOMBRE, "Hombre"),
    Opcion(GENERO_MUJER, "Mujer")
)

val RANGOS_ETARIOS = listOf(
    Opcion(RANGO_NINO, "Niño/adolescente", "0 - 17"),
    Opcion(RANGO_ADULTO, "Adulto", "18 - 64"),
    Opcion(RANGO_MAYOR, "Adulto mayor", "65+")
)

val TIPOS_REGISTRO = listOf(
    Opcion(TIPO_TRANSITO, "Transitó", "sin compra"),
    Opcion(TIPO_COMPRO, "Compró", "realizó compra")
)

fun List<Opcion>.etiquetaDe(id: Int): String = firstOrNull { it.id == id }?.etiqueta ?: "—"

