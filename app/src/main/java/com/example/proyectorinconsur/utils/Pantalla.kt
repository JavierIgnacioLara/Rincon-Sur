package com.example.proyectorinconsur.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun esHorizontal(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

@Composable
fun esPantallaAncha(): Boolean {
    val configuracion = LocalConfiguration.current
    return configuracion.orientation == Configuration.ORIENTATION_LANDSCAPE || configuracion.screenWidthDp >= 600
}
