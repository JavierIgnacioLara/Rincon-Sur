package com.example.proyectorinconsur.domain.model

data class RegistrarPersonaErrores(
    val entrada: String? = null,
    val genero: String? = null,
    val rango: String? = null,
    val tipo: String? = null,
    val salida: String? = null,
    val acompanantes: String? = null
) {
    val sinErrores: Boolean get() = listOfNotNull(entrada, genero, rango, tipo, salida, acompanantes).isEmpty()
}
