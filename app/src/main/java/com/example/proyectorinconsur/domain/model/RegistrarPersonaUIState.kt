package com.example.proyectorinconsur.domain.model

data class RegistrarPersonaUIState(
    val entradaId: Int? = null,
    val generoId: Int? = null,
    val rangoId: Int? = null,
    val tipoId: Int? = null,
    val salidaId: Int? = null,
    val acompAdultos: Int = 0,          // opcional: 0 = sin acompañantes adultos
    val acompNinos: Int = 0,            // opcional: 0 = sin acompañantes niños
    val errores: RegistrarPersonaErrores = RegistrarPersonaErrores(),
    val turnoActivo: Turno? = null,
    val isSubmitting: Boolean = false,
    val registroExitoso: Boolean = false,
    val errorMsg: String? = null
)
