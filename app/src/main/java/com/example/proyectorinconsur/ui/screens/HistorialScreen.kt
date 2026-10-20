package com.example.proyectorinconsur.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectorinconsur.model.ACCESOS
import com.example.proyectorinconsur.ui.components.DialogoFecha
import com.example.proyectorinconsur.model.GENEROS
import com.example.proyectorinconsur.model.HistorialUIState
import com.example.proyectorinconsur.viewmodel.HistorialViewModel
import com.example.proyectorinconsur.ui.components.ListaEstadisticas
import com.example.proyectorinconsur.model.RANGOS_ETARIOS
import com.example.proyectorinconsur.model.Registro
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.model.TIPO_COMPRO
import com.example.proyectorinconsur.utils.esPantallaAncha
import com.example.proyectorinconsur.model.etiquetaDe
import com.example.proyectorinconsur.utils.formatFechaLarga
import com.example.proyectorinconsur.utils.formatFechaNumerica
import com.example.proyectorinconsur.utils.formatHora
import com.example.proyectorinconsur.ui.layouts.ScaffoldAdaptable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    onSeleccionarPestana: (Screen) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var mostrarCalendario by rememberSaveable { mutableStateOf(false) }

    if (mostrarCalendario) {
        DialogoFecha(
            fechaInicialLocal = estado.fechaMillis,
            onConfirmar = { dia ->
                viewModel.seleccionarFecha(dia)
                mostrarCalendario = false
            },
            onCancelar = { mostrarCalendario = false }
        )
    }

    ScaffoldAdaptable(
        actual = Screen.Historial,
        onSeleccionarPestana = onSeleccionarPestana,
        topBar = {
            TopAppBar(title = {
                Text("Historial", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            })
        },
    ) { innerPadding ->
        if (esPantallaAncha()) {

            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SelectorFecha(formatFechaLarga(estado.fechaMillis)) { mostrarCalendario = true }
                    FilaPestanas(estado.accesoSeleccionado, viewModel::seleccionarAcceso)
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    contenidoHistorial(estado)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { SelectorFecha(formatFechaLarga(estado.fechaMillis)) { mostrarCalendario = true } }
                item { FilaPestanas(estado.accesoSeleccionado, viewModel::seleccionarAcceso) }
                contenidoHistorial(estado)
            }
        }
    }
}

private fun LazyListScope.contenidoHistorial(estado: HistorialUIState) {
    if (estado.accesoSeleccionado == 0) {
        item {
            Column {
                Text("Resumen del día", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Totales generales (todos los accesos)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (estado.registrosDelDia.isEmpty()) {
            item { EstadoVacio("No hay registros en esta fecha.") }
        } else {
            item { ListaEstadisticas(estado.resumen) }
        }
    } else {
        val registros = estado.registrosDelAcceso
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Registros del Acceso ${estado.accesoSeleccionado}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text("Total: ${registros.size}", style = MaterialTheme.typography.titleSmall)
            }
        }
        if (registros.isEmpty()) {
            item { EstadoVacio("No hay registros del Acceso ${estado.accesoSeleccionado} en esta fecha.") }
        } else {
            items(registros, key = { it.id }) { registro -> FilaRegistro(registro) }
        }
    }
}

@Composable
private fun SelectorFecha(texto: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(Modifier.width(10.dp))
            Text(
                texto,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Cambiar fecha")
        }
    }
}


@Composable
private fun FilaPestanas(seleccionado: Int, onSeleccionar: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        ACCESOS.forEach { acceso ->
            Pestana(
                texto = acceso.etiqueta,
                seleccionada = seleccionado == acceso.id,
                onClick = { onSeleccionar(acceso.id) },
                modifier = Modifier.weight(1f)
            )
        }
        Pestana(
            texto = "Total",
            seleccionada = seleccionado == 0,
            onClick = { onSeleccionar(0) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Pestana(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (seleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
            color = if (seleccionada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
private fun FilaRegistro(registro: Registro) {
    val esCompra = registro.tipoRegistroId == TIPO_COMPRO
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.width(84.dp)) {
                Text(formatHora(registro.fechaHora), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    formatFechaNumerica(registro.fechaHora),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${GENEROS.etiquetaDe(registro.generoId)} · ${RANGOS_ETARIOS.etiquetaDe(registro.rangoEtarioId)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Salió A${registro.accesoSalidaId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Solo aparece si la persona fue con acompañantes
                if (registro.acompAdultos > 0 || registro.acompNinos > 0) {
                    Text(
                        "Acompañantes: ${registro.acompAdultos} adulto(s) · ${registro.acompNinos} niño(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(50),
                color = if (esCompra) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    if (esCompra) "Compró" else "Transitó",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (esCompra) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun EstadoVacio(texto: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(texto, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}


