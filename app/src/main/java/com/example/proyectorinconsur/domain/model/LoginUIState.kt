package com.example.proyectorinconsur.domain.model

data class LoginUIState (
    val correo: String = "",
    val clave: String = "",
    val recordar: Boolean = false,
    val errores: LoginErrores = LoginErrores(),
    val isSubmitting: Boolean = false,
    val errorMsg: String? = null,
    val inicioExitoso: Boolean = false

)