package com.example.proyectorinconsur.ui.layouts

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.navigation.destino

private data class Destino(val pantalla: Screen, val etiqueta: String, val icono: ImageVector)

private val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
    Destino(Screen.Accesos, "Accesos", Icons.Default.MeetingRoom),
    Destino(Screen.Historial, "Historial", Icons.Default.History)
)

@Composable
fun BarraInferior(
    actual: Screen,
    onSeleccionar: (Screen) -> Unit
) {
    NavigationBar {
        DESTINOS.forEach { destino ->
            NavigationBarItem(
                selected = actual == destino.pantalla,
                onClick = { onSeleccionar(destino.pantalla) },
                icon = { Icon(destino.icono, contentDescription = null) },
                label = { Text(destino.etiqueta) }
            )
        }
    }
}

@Composable
fun RielNavegacion(
    actual: Screen,
    onSeleccionar: (Screen) -> Unit
) {
    NavigationRail {
        Spacer(Modifier.weight(1f))
        DESTINOS.forEach { destino ->
            NavigationRailItem(
                selected = actual == destino.pantalla,
                onClick = { onSeleccionar(destino.pantalla) },
                icon = { Icon(destino.icono, contentDescription = null) },
                label = { Text(destino.etiqueta) }
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

