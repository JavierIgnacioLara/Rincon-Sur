package com.example.proyectorinconsur.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.utils.esPantallaAncha

@Composable
fun ScaffoldAdaptable(
    actual: Screen,
    onSeleccionarPestana: (Screen) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    accionesInferiores: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    if (esPantallaAncha()) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            RielNavegacion(actual = actual, onSeleccionar = onSeleccionarPestana)
            Scaffold(
                modifier = Modifier
                    .weight(1f)
                    .consumeWindowInsets(WindowInsets.systemBars.only(WindowInsetsSides.Start)),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = topBar,
                bottomBar = accionesInferiores,
                snackbarHost = snackbarHost,
                content = content
            )
        }
    } else {
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
            topBar = topBar,
            bottomBar = {
                androidx.compose.foundation.layout.Column {
                    accionesInferiores()
                    BarraInferior(actual = actual, onSeleccionar = onSeleccionarPestana)
                }
            },
            snackbarHost = snackbarHost,
            content = content
        )
    }
}