package com.example.proyectorinconsur.navigation

const val RUTA_DETALLE_ACCESO = "detalle_acceso/{accesoId}"

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object CrearUsuario : Screen("crear_usuario")

    data object Inicio : Screen("inicio")
    data object Accesos : Screen("accesos")
    data object Historial : Screen("historial")

    data object RegistrarPersona : Screen("registrar_persona")

    data class DetalleAcceso(val accesoId: Int) : Screen(RUTA_DETALLE_ACCESO) {
        fun buildRoute(): String = "detalle_acceso/$accesoId"
    }
}

fun Screen.destino(): String = if (this is Screen.DetalleAcceso) buildRoute() else route