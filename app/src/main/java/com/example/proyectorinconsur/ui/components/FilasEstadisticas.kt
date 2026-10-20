package com.example.proyectorinconsur.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.proyectorinconsur.model.Estadisticas
import com.example.proyectorinconsur.ui.theme.ColorCompra
import com.example.proyectorinconsur.ui.theme.ColorHombre
import com.example.proyectorinconsur.ui.theme.ColorMujer
import com.example.proyectorinconsur.ui.theme.ColorTransito

@Composable
private fun FilaEstadistica(
    icono: ImageVector,
    tinte: Color,
    titulo: String,
    detalle: String?,
    valor: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(tinte.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = tinte, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.bodyLarge)
            if (detalle != null) {
                Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        NumeroAnimado(valor, estilo = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun Separador() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

@Composable
fun ListaEstadisticas(e: Estadisticas, modifier: Modifier = Modifier) {
    val primario = MaterialTheme.colorScheme.primary
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            FilaEstadistica(Icons.Default.People, primario, "Total de personas", null, e.total)
            Separador()
            FilaEstadistica(Icons.AutoMirrored.Filled.ArrowForward, ColorTransito, "Tránsito", "(sin compra)", e.transito)
            Separador()
            FilaEstadistica(Icons.Default.ShoppingCart, ColorCompra, "Compró", "(realizó compra)", e.compro)
            Separador()
            FilaEstadistica(Icons.Default.Person, ColorHombre, "Género masculino", null, e.hombres)
            Separador()
            FilaEstadistica(Icons.Default.Person, ColorMujer, "Género femenino", null, e.mujeres)
            Separador()
            FilaEstadistica(Icons.Default.ChildCare, primario, "Niño/adolescente", "(0 - 17)", e.ninos)
            Separador()
            FilaEstadistica(Icons.Default.Person, primario, "Adulto", "(18 - 64)", e.adultos)
            Separador()
            FilaEstadistica(Icons.Default.Face, primario, "Adulto mayor", "(65+)", e.mayores)
            Separador()
            FilaEstadistica(Icons.Default.Person, ColorTransito, "Acompañantes de tránsito", "adultos", e.acompTransitoAdultos)
            Separador()
            FilaEstadistica(Icons.Default.ChildCare, ColorTransito, "Acompañantes de tránsito", "niños", e.acompTransitoNinos)
            Separador()
            FilaEstadistica(Icons.Default.Person, ColorCompra, "Acompañantes de compra", "adultos", e.acompCompraAdultos)
            Separador()
            FilaEstadistica(Icons.Default.ChildCare, ColorCompra, "Acompañantes de compra", "niños", e.acompCompraNinos)
        }
    }
}

