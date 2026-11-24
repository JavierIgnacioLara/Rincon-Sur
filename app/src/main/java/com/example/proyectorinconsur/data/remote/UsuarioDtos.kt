package com.example.proyectorinconsur.data.remote

data class LoginRequestDto(val correo: String, val clave: String)

data class UsuarioRequestDto(
    val nombre: String,
    val correo: String,
    val clave: String?,
    val rol: String?
)

data class UsuarioDto(
    val id: Long,
    val nombre: String,
    val correo: String,
    val rol: String,
    val creadoEn: Long
)

/** Forma del error que devuelve el backend: { "mensaje": "..." } */
data class ErrorDto(val mensaje: String?)
