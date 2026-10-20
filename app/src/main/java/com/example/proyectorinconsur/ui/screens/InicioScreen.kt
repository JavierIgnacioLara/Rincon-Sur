package com.example.proyectorinconsur.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectorinconsur.ui.components.CajaInformativa
import com.example.proyectorinconsur.ui.layouts.ContenedorAdaptable
import com.example.proyectorinconsur.viewmodel.InicioViewModel
import com.example.proyectorinconsur.ui.components.MiniaturaFoto
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.utils.abrirAjustesDeLaApp
import com.example.proyectorinconsur.utils.crearUriFoto
import com.example.proyectorinconsur.utils.formatFechaLarga
import com.example.proyectorinconsur.utils.formatHora
import com.example.proyectorinconsur.ui.components.rememberAhora
import com.example.proyectorinconsur.utils.tienePermisoCamara
import com.example.proyectorinconsur.utils.permisoCamaraDeclarado
import com.example.proyectorinconsur.utils.esPantallaAncha
import com.example.proyectorinconsur.ui.layouts.ScaffoldAdaptable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    onIrARegistrar: () -> Unit,
    onSeleccionarPestana: (Screen) -> Unit,
    onSesionCerrada: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val usuarioId by viewModel.usuarioId.collectAsStateWithLifecycle()
    val ahora by rememberAhora()
    val context = LocalContext.current


    var explicarCamara by rememberSaveable { mutableStateOf(false) }
    var permisoCamaraDenegado by rememberSaveable { mutableStateOf(false) }
    var confirmarCierre by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(usuarioId) { if (usuarioId == null) onSesionCerrada() }

    // Al empezar el turno se pasa directo a «Registrar persona»
    LaunchedEffect(estado.turnoIniciado) {
        if (estado.turnoIniciado) {
            viewModel.consumirTurnoIniciado()
            onIrARegistrar()
        }
    }


    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(estado.mensaje, estado.errorMsg) {
        val texto = estado.errorMsg ?: estado.mensaje
        if (texto != null) {
            snackbarHostState.showSnackbar(texto)
            viewModel.consumirMensajes()
        }
    }


    val camaraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        viewModel.resultadoCamara(exito)
    }

    fun abrirCamara() {
        try {
            val uri = crearUriFoto(context)
            viewModel.prepararCamara(uri.toString())
            camaraLauncher.launch(uri)
        } catch (e: Exception) {

            viewModel.mostrarError("No se pudo abrir la cámara. Revisa el FileProvider en AndroidManifest.xml y res/xml/file_paths.xml.")
        }
    }

    val permisoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) {
            permisoCamaraDenegado = false
            abrirCamara()
        } else {
            permisoCamaraDenegado = true
            // Si el permiso ni siquiera está declarado, Android no muestra ningún diálogo
            if (!permisoCamaraDeclarado(context)) {
                viewModel.mostrarError("Falta declarar el permiso CAMERA en AndroidManifest.xml. Agrégalo y vuelve a instalar la app.")
            }
        }
    }

    if (explicarCamara) {
        AlertDialog(
            onDismissRequest = { explicarCamara = false },
            title = { Text("Permiso de cámara") },
            text = {
                Text(
                    "Rincón Sur usa la cámara solo para tomar una foto opcional del turno o del acceso. " +
                            "No se pide ningún otro permiso y puedes continuar sin foto."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    explicarCamara = false
                    permisoLauncher.launch(Manifest.permission.CAMERA)
                }) { Text("Continuar") }
            },
            dismissButton = { TextButton(onClick = { explicarCamara = false }) { Text("Ahora no") } }
        )
    }

    if (confirmarCierre) {
        AlertDialog(
            onDismissRequest = { confirmarCierre = false },
            title = { Text("Cerrar turno") },
            text = { Text("Se guardará la hora de término. Los registros del turno se conservan en el historial.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarCierre = false
                    viewModel.cerrarTurno()
                }) { Text("Cerrar turno") }
            },
            dismissButton = { TextButton(onClick = { confirmarCierre = false }) { Text("Cancelar") } }
        )
    }

    val turnoActivo = estado.turnoActivo

    ScaffoldAdaptable(
        actual = Screen.Inicio,
        onSeleccionarPestana = onSeleccionarPestana,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            if (turnoActivo == null) "Iniciar turno" else "Turno activo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Rincón Sur",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = { TextButton(onClick = viewModel::cerrarSesion) { Text("Salir") } }
            )
        },

        accionesInferiores = {
            Column {
                BarraAccionesTurno(
                    hayTurnoActivo = turnoActivo != null,
                    enProceso = estado.isSubmitting,
                    onEmpezar = viewModel::empezarTurno,
                    onIrARegistrar = onIrARegistrar,
                    onCerrar = { confirmarCierre = true }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        ContenedorAdaptable(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            espacio = 12.dp,
            izquierda = {
                Text(
                    "Completa los datos para comenzar el registro de accesos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FilaDato(Icons.Default.Person, "Nombre completo") {
                    Text(estado.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                FilaDato(Icons.Default.DateRange, "Fecha actual") {
                    Text(formatFechaLarga(ahora), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                FilaDato(
                    Icons.Default.AccessTime,
                    if (turnoActivo == null) "Hora de inicio" else "Turno iniciado a las"
                ) {
                    Text(
                        formatHora(turnoActivo?.horaInicio ?: ahora),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            derecha = {

                TarjetaRecurso(
                    icono = Icons.Default.CameraAlt,
                    titulo = "Foto opcional",
                    descripcion = "Tomar foto solo si el usuario lo desea",
                    textoBoton = if (estado.fotoMostrada == null) "Tomar foto" else "Repetir",
                    onBoton = {
                        if (tienePermisoCamara(context)) abrirCamara() else explicarCamara = true
                    }
                ) {
                    val foto = estado.fotoMostrada
                    if (foto != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MiniaturaFoto(uri = foto, modifier = Modifier.size(72.dp))
                            Spacer(Modifier.width(12.dp))
                            Text("Foto capturada", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                            TextButton(onClick = viewModel::quitarFoto) { Text("Quitar") }
                        }
                    }
                }

                if (permisoCamaraDenegado && !tienePermisoCamara(context)) {
                    CajaInformativa("Sin el permiso de cámara no se puede tomar la foto. Puedes continuar sin ella o habilitar el permiso en Ajustes.")
                    TextButton(onClick = { abrirAjustesDeLaApp(context) }) { Text("Abrir ajustes") }
                }

                if (turnoActivo == null) {
                    CajaInformativa("Una vez que inicies el turno podrás registrar personas y su información.")
                } else {
                    CajaInformativa("Tienes un turno activo. Puedes seguir registrando personas o cerrarlo.")
                }

                Spacer(Modifier.height(8.dp))
            }
        )
    }
}

@Composable
private fun FilaDato(
    icono: ImageVector,
    titulo: String,
    contenido: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                Text(titulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                contenido()
            }
        }
    }
}

@Composable
private fun TarjetaRecurso(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    textoBoton: String,
    onBoton: () -> Unit,
    extra: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                FilledTonalButton(onClick = onBoton) { Text(textoBoton) }
            }
            extra()
        }
    }
}


@Composable
private fun BarraAccionesTurno(
    hayTurnoActivo: Boolean,
    enProceso: Boolean,
    onEmpezar: () -> Unit,
    onIrARegistrar: () -> Unit,
    onCerrar: () -> Unit
) {
    val principal: @Composable (Modifier) -> Unit = { modificador ->
        Button(
            onClick = if (hayTurnoActivo) onIrARegistrar else onEmpezar,
            enabled = !enProceso,
            shape = RoundedCornerShape(14.dp),
            modifier = modificador.height(52.dp)
        ) {
            if (enProceso) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else if (hayTurnoActivo) {
                Icon(Icons.Default.People, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Registrar personas", style = MaterialTheme.typography.titleMedium)
            } else {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Empezar turno", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    val secundario: @Composable (Modifier) -> Unit = { modificador ->
        OutlinedButton(
            onClick = onCerrar,
            enabled = hayTurnoActivo && !enProceso,
            shape = RoundedCornerShape(14.dp),
            modifier = modificador.height(52.dp)
        ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar turno", style = MaterialTheme.typography.titleMedium)
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 6.dp
    ) {
        if (esPantallaAncha()) {
            // En horizontal: uno al lado del otro para ocupar menos altura
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                principal(Modifier.weight(1f))
                secundario(Modifier.weight(1f))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                principal(Modifier.fillMaxWidth())
                secundario(Modifier.fillMaxWidth())
            }
        }
    }
}




