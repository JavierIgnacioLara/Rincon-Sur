package com.example.proyectorinconsur.domain.model

class UsuarioUIState (
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmacion: String = "",
    val errores: UsuarioErrores = UsuarioErrores(),
    val esPrimerUsuario: Boolean = false,
    val isSubmitting: Boolean = false,
    val cuentaCreada: Boolean = false,
    val errorMsg: String? = null

)