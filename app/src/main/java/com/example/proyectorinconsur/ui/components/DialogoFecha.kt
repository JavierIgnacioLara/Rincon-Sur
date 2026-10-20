package com.example.proyectorinconsur.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.proyectorinconsur.utils.localAUtcMedianoche
import com.example.proyectorinconsur.utils.utcMedianocheALocal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoFecha(
    fechaInicialLocal: Long,
    onConfirmar: (Long) -> Unit,
    onCancelar: () -> Unit
) {

    val estado = rememberDatePickerState(initialSelectedDateMillis = localAUtcMedianoche(fechaInicialLocal))

    DatePickerDialog(
        onDismissRequest = onCancelar,
        confirmButton = {
            TextButton(onClick = {
                val elegida = estado.selectedDateMillis
                if (elegida != null) onConfirmar(utcMedianocheALocal(elegida)) else onCancelar()
            }) { Text("Aceptar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    ) {
        DatePicker(state = estado)
    }
}

