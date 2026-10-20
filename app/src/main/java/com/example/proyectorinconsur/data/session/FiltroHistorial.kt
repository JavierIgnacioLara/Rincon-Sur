package com.example.proyectorinconsur.data.session

import com.example.proyectorinconsur.utils.inicioDelDia
import kotlinx.coroutines.flow.MutableStateFlow

class FiltroHistorial {
    val fecha = MutableStateFlow(inicioDelDia(System.currentTimeMillis()))
    val acceso = MutableStateFlow(0)
}