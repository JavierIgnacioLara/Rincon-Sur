package com.example.proyectorinconsur.domain.model

const val ROL_ADMIN = "ADMIN"
const val ROL_OPERADOR = "OPERADOR"

data class Usuario(
    val id: Long,
    val nombre: String,
    val correo: String,
    val rol: String,
    val creadoEn: Long
)