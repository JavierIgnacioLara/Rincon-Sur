package com.example.proyectorinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectorinconsur.model.ACCESOS
import com.example.proyectorinconsur.ui.components.CajaInformativa
import com.example.proyectorinconsur.ui.layouts.ContenedorAdaptable
import com.example.proyectorinconsur.model.GENEROS
import com.example.proyectorinconsur.ui.components.GrupoOpciones
import com.example.proyectorinconsur.ui.components.MensajeError
import com.example.proyectorinconsur.ui.components.MensajeExito
import com.example.proyectorinconsur.ui.components.SelectorAcompanantes
import com.example.proyectorinconsur.model.RANGOS_ETARIOS
import com.example.proyectorinconsur.model.RegistrarPersonaUIState
import com.example.proyectorinconsur.viewmodel.RegistrarPersonaViewModel
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.model.TIPOS_REGISTRO
import com.example.proyectorinconsur.ui.components.iconoAcceso
import com.example.proyectorinconsur.ui.components.iconoGenero
import com.example.proyectorinconsur.ui.components.iconoRango
import com.example.proyectorinconsur.ui.components.iconoTipo
import com.example.proyectorinconsur.ui.layouts.ScaffoldAdaptable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarPersonaScreen(
    viewModel: RegistrarPersonaViewModel,
    onVolver: () -> Unit,
    onIrAInicio: () -> Unit,
    onSeleccionarPestana: (Screen) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(estado.registroExitoso) {
        if (estado.registroExitoso) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    ScaffoldAdaptable(
        actual = Screen.Inicio,
        onSeleccionarPestana = onSeleccionarPestana,
        topBar = {
            TopAppBar(
                title = {
                    Text("Registrar persona", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },

        accionesInferiores = {
            Column {
                BarraAccionesRegistro(
                    estado = estado,
                    onRegistrar = viewModel::registrar
                )
            }
        }
    ) { innerPadding ->

        ContenedorAdaptable(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            espacio = 12.dp,
            izquierda = {

                if (estado.turnoActivo == null) {
                    CajaInformativa("No hay un turno activo. Inicia un turno para poder registrar personas.")
                    OutlinedButton(
                        onClick = onIrAInicio,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Ir a Inicio") }
                }

                GrupoOpciones(
                    titulo = "Acceso de entrada",
                    opciones = ACCESOS,
                    seleccionId = estado.entradaId,
                    onSeleccion = viewModel::onEntrada,
                    icono = ::iconoAcceso,
                    error = estado.errores.entrada
                )
                GrupoOpciones(
                    titulo = "Género",
                    opciones = GENEROS,
                    seleccionId = estado.generoId,
                    onSeleccion = viewModel::onGenero,
                    icono = ::iconoGenero,
                    error = estado.errores.genero
                )
                GrupoOpciones(
                    titulo = "Rango de edad",
                    opciones = RANGOS_ETARIOS,
                    seleccionId = estado.rangoId,
                    onSeleccion = viewModel::onRango,
                    icono = ::iconoRango,
                    error = estado.errores.rango
                )
            },
            derecha = {
                GrupoOpciones(
                    titulo = "Tipo de registro",
                    opciones = TIPOS_REGISTRO,
                    seleccionId = estado.tipoId,
                    onSeleccion = viewModel::onTipo,
                    icono = ::iconoTipo,
                    error = estado.errores.tipo
                )

                SelectorAcompanantes(
                    adultos = estado.acompAdultos,
                    ninos = estado.acompNinos,
                    onAdultos = viewModel::onAcompAdultos,
                    onNinos = viewModel::onAcompNinos,
                    error = estado.errores.acompanantes
                )
                GrupoOpciones(
                    titulo = "Acceso de salida",
                    opciones = ACCESOS,
                    seleccionId = estado.salidaId,
                    onSeleccion = viewModel::onSalida,
                    icono = ::iconoAcceso,
                    error = estado.errores.salida
                )
                Spacer(Modifier.height(8.dp))
            }
        )
    }
}

@Composable
private fun BarraAccionesRegistro(
    estado: RegistrarPersonaUIState,
    onRegistrar: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MensajeError(estado.errorMsg)
            MensajeExito(visible = estado.registroExitoso, texto = "Persona registrada correctamente")

            Button(
                onClick = onRegistrar,
                enabled = !estado.isSubmitting,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (estado.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrar persona", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}




