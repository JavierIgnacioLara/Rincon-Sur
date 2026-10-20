package com.example.proyectorinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectorinconsur.ui.components.CajaInformativa
import com.example.proyectorinconsur.ui.theme.ColoresAcceso
import com.example.proyectorinconsur.ui.layouts.ContenedorAdaptable
import com.example.proyectorinconsur.viewmodel.DetalleAccesoViewModel
import com.example.proyectorinconsur.ui.components.ListaEstadisticas
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.ui.layouts.ScaffoldAdaptable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleAccesoScreen(
    accesoId: Int,
    viewModel: DetalleAccesoViewModel,
    onVolver: () -> Unit,
    onVerHistorial: () -> Unit,
    onSeleccionarPestana: (Screen) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val color = ColoresAcceso[(accesoId - 1).coerceIn(0, ColoresAcceso.lastIndex)]

    ScaffoldAdaptable(
        actual = Screen.Accesos,
        onSeleccionarPestana = onSeleccionarPestana,
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalle de Acceso $accesoId", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
    ) { innerPadding ->

        ContenedorAdaptable(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            espacio = 14.dp,
            izquierda = {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = color, modifier = Modifier.size(36.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Acceso $accesoId", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }

                if (!estado.hayTurnoActivo) {
                    CajaInformativa("No hay un turno activo. Aquí se muestran los totales del turno en curso.")
                }
            },
            derecha = {
                ListaEstadisticas(estado.estadisticas)

                OutlinedButton(
                    onClick = {
                        viewModel.prepararHistorial()    // deja el historial filtrado por este acceso
                        onVerHistorial()
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ver registros de este acceso", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(8.dp))
            }
        )
    }
}

