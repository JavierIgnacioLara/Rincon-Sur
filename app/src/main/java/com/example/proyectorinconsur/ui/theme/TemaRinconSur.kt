package com.example.proyectorinconsur.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaTurquesa = lightColorScheme(
    primary = Color(0xFF17A097),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCFCFB),
    onPrimaryContainer = Color(0xFF0B4F4A),
    secondary = Color(0xFF528C8A),
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF1E1F21),
    surface = Color.White,
    onSurface = Color(0xFF1E1F21),
    surfaceVariant = Color(0xFFEBEDEE),
    onSurfaceVariant = Color(0xFF5A6468),
    outline = Color(0xFFB1C3C7),
    outlineVariant = Color(0xFFD3E6EA),
    error = Color(0xFFD93A3A),
    onError = Color.White,
    errorContainer = Color(0xFFFDECEC),
    onErrorContainer = Color(0xFF7A1B1B)
)

val ColoresAcceso = listOf(
    Color(0xFF17A097),
    Color(0xFF7B4FD6),
    Color(0xFFF29A1F),
    Color(0xFFE0443E)
)

@Composable
fun TemaRinconSur(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaTurquesa, content = content)
}


val ColorTransito = Color(0xFF2E9E5B)
val ColorCompra = Color(0xFF17A097)
val ColorHombre = Color(0xFF17A097)
val ColorMujer = Color(0xFFE0559C)
