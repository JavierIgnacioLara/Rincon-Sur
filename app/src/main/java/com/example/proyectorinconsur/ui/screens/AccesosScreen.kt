package com.example.proyectorinconsur.ui.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectorinconsur.model.ACCESOS
import com.example.proyectorinconsur.viewmodel.AccesosViewModel
import com.example.proyectorinconsur.ui.components.CajaInformativa
import com.example.proyectorinconsur.ui.theme.ColorCompra
import com.example.proyectorinconsur.ui.theme.ColorTransito
import com.example.proyectorinconsur.ui.theme.ColoresAcceso
import com.example.proyectorinconsur.ui.layouts.ContenedorAdaptable
import com.example.proyectorinconsur.model.Estadisticas
import com.example.proyectorinconsur.ui.components.NumeroAnimado
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.ui.layouts.ScaffoldAdaptable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccesosScreen(
    viewModel: AccesosViewModel,
    onVerDetalle: (accesoId: Int) -> Unit,
    onSeleccionarPestana: (Screen) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    ScaffoldAdaptable(
        actual = Screen.Accesos,
        onSeleccionarPestana = onSeleccionarPestana,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Accesos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "Resumen en tiempo real",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        ContenedorAdaptable(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            espacio = 12.dp,
            izquierda = {
                if (!estado.hayTurnoActivo) {
                    CajaInformativa("No hay un turno activo. Inicia un turno para ver las estadísticas en tiempo real.")
                }

                // Las cuatro tarjetas de acceso, de a dos por fila
                ACCESOS.chunked(2).forEach { fila ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        fila.forEach { acceso ->
                            TarjetaAcceso(
                                nombre = acceso.etiqueta,
                                color = ColoresAcceso[(acceso.id - 1) % ColoresAcceso.size],
                                estadisticas = estado.de(acceso.id),
                                onVerDetalle = { onVerDetalle(acceso.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            derecha = {
                TarjetaTotal(estado.resumen)
                Spacer(Modifier.height(8.dp))
            }
        )
    }
}

@Composable
private fun TarjetaAcceso(
    nombre: String,
    color: Color,
    estadisticas: Estadisticas,
    onVerDetalle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(8.dp))
                Text(nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            NumeroAnimado(estadisticas.total, estilo = MaterialTheme.typography.headlineLarge)
            Text("personas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "Tránsito ${estadisticas.transito} · Compró ${estadisticas.compro}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Acomp. tránsito ${estadisticas.acompTransito} · compra ${estadisticas.acompCompra}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FilledTonalButton(
                onClick = onVerDetalle,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver detalle", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}

@Composable
private fun TarjetaTotal(e: Estadisticas) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.People,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Total del turno", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        NumeroAnimado(e.total, estilo = MaterialTheme.typography.displaySmall)
                        Spacer(Modifier.width(8.dp))
                        Text("personas", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ResumenMini(Icons.AutoMirrored.Filled.ArrowForward, ColorTransito, "Tránsito", e.transito)
                ResumenMini(Icons.Default.ShoppingCart, ColorCompra, "Compró", e.compro)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ResumenMini(Icons.Default.People, ColorTransito, "Acomp. tránsito", e.acompTransito)
                ResumenMini(Icons.Default.People, ColorCompra, "Acomp. compra", e.acompCompra)
            }
        }
    }
}

@Composable
private fun ResumenMini(
    icono: ImageVector,
    tinte: Color,
    titulo: String,
    valor: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = tinte, modifier = Modifier.size(30.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
            NumeroAnimado(valor, estilo = MaterialTheme.typography.headlineSmall)
        }
    }
}

