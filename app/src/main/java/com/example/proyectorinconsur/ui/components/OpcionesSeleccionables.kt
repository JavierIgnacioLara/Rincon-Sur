package com.example.proyectorinconsur.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectorinconsur.model.Opcion
import com.example.proyectorinconsur.model.TIPO_COMPRO

// ---- Íconos de cada opción (si alguno no existe en tu versión, cámbialo aquí) ----
fun iconoAcceso(@Suppress("UNUSED_PARAMETER") opcion: Opcion): ImageVector = Icons.Default.MeetingRoom
fun iconoGenero(@Suppress("UNUSED_PARAMETER") opcion: Opcion): ImageVector = Icons.Default.Person
fun iconoRango(opcion: Opcion): ImageVector = when (opcion.id) {
    1 -> Icons.Default.ChildCare
    2 -> Icons.Default.Person
    else -> Icons.Default.Face
}
fun iconoTipo(opcion: Opcion): ImageVector =
    if (opcion.id == TIPO_COMPRO) Icons.Default.ShoppingCart else Icons.AutoMirrored.Filled.ArrowForward


@Composable
fun OpcionSeleccionable(
    texto: String,
    detalle: String?,
    icono: ImageVector,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val fondo by animateColorAsState(
        targetValue = if (seleccionada) colores.primary else colores.surfaceVariant,
        animationSpec = tween(200),
        label = "fondo"
    )
    val contenido by animateColorAsState(
        targetValue = if (seleccionada) colores.onPrimary else colores.onSurface,
        animationSpec = tween(200),
        label = "contenido"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = fondo,
        modifier = modifier
            .heightIn(min = 60.dp)
            .selectable(selected = seleccionada, onClick = onClick, role = Role.RadioButton)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(icono, contentDescription = null, tint = contenido, modifier = Modifier.size(22.dp))
                Spacer(Modifier.height(2.dp))
                Text(
                    texto,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                    color = contenido,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
                if (detalle != null) {
                    Text(
                        "($detalle)",
                        style = MaterialTheme.typography.labelSmall,
                        color = contenido,
                        textAlign = TextAlign.Center
                    )
                }
            }
            AnimatedVisibility(
                visible = seleccionada,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = colores.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Composable
fun GrupoOpciones(
    titulo: String,
    opciones: List<Opcion>,
    seleccionId: Int?,
    onSeleccion: (Int) -> Unit,
    icono: (Opcion) -> ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(modifier = modifier) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            opciones.forEach { opcion ->
                OpcionSeleccionable(
                    texto = opcion.etiqueta,
                    detalle = opcion.detalle,
                    icono = icono(opcion),
                    seleccionada = opcion.id == seleccionId,
                    onClick = { onSeleccion(opcion.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

