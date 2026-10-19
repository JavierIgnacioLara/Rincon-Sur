package com.example.proyectorinconsur.model

data class RegistrarPersonaUIState(
    val entradaId: Int? = null,
    val generoId: Int? = null,
    val rangoId: Int? = null,
    val tipoId: Int? = null,
    val salidaId: Int? = null,
    val acompAdultos: Int = 0,
    val acompNinos: Int = 0,
    val errores: RegistrarPersonaErrores = RegistrarPersonaErrores(),
    val turnoActivo: Turno? = null,
    val isSubmitting: Boolean = false,
    val registroExitoso: Boolean = false,
    val errorMsg: String? = null
)
