package com.example.proyectorinconsur.domain.model

data class InicioUIState (
    val nombre: String = "",
    val turnoActivo: Turno? = null,
    val fotoPendiente: String? = null,
    val camaraUriPendiente: String? = null,
    val isSubmitting: Boolean = false,
    val turnoIniciado: Boolean = false,
    val mensaje: String? = null,
    val errorMsg: String? = null
) {
    val fotoMostrada: String? get() = turnoActivo?.fotoUri ?: fotoPendiente
}

