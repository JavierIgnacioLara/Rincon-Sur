package com.example.proyectorinconsur.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.proyectorinconsur.utils.esPantallaAncha

@Composable
fun ContenedorAdaptable(
    modifier: Modifier = Modifier,
    espacio: Dp = 16.dp,
    alineacion: Alignment.Horizontal = Alignment.Start,
    izquierda: @Composable ColumnScope.() -> Unit,
    derecha: @Composable ColumnScope.() -> Unit
) {
    if (esPantallaAncha()) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(espacio)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(espacio),
                horizontalAlignment = alineacion,
                content = izquierda
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(espacio),
                horizontalAlignment = alineacion,
                content = derecha
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(espacio),
            horizontalAlignment = alineacion
        ) {
            izquierda()
            derecha()
        }
    }
}
