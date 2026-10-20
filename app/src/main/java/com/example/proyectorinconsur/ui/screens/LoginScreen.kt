package com.example.proyectorinconsur.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectorinconsur.ui.components.CampoTexto
import com.example.proyectorinconsur.ui.layouts.ContenedorAdaptable
import com.example.proyectorinconsur.viewmodel.LoginViewModel
import com.example.proyectorinconsur.ui.components.MensajeError

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIrACrearCuenta: () -> Unit,
    onLoginExitoso: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current


    LaunchedEffect(estado.inicioExitoso) {
        if (estado.inicioExitoso) {
            viewModel.consumirInicioExitoso()
            onLoginExitoso()
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->

        ContenedorAdaptable(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            espacio = 14.dp,
            alineacion = Alignment.CenterHorizontally,
            izquierda = {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(88.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }
                Text("Rincón Sur", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Tienda Departamental",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            derecha = {
                CampoTexto(
                    valor = estado.correo,
                    onCambio = viewModel::onCorreoChange,
                    etiqueta = "Correo electrónico",
                    icono = Icons.Default.Email,
                    error = estado.errores.correo,
                    tipoTeclado = KeyboardType.Email
                )

                CampoTexto(
                    valor = estado.clave,
                    onCambio = viewModel::onClaveChange,
                    etiqueta = "Contraseña",
                    icono = Icons.Default.Lock,
                    error = estado.errores.clave,
                    esClave = true,
                    accionIme = ImeAction.Done,
                    alAccionIme = {
                        foco.clearFocus()
                        viewModel.iniciarSesion()
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = estado.recordar,
                            onValueChange = viewModel::onRecordarChange,
                            role = Role.Checkbox
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = estado.recordar, onCheckedChange = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Recordar sesión", style = MaterialTheme.typography.bodyLarge)
                }

                MensajeError(estado.errorMsg)

                Button(
                    onClick = {
                        foco.clearFocus()
                        viewModel.iniciarSesion()
                    },
                    enabled = !estado.isSubmitting,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    if (estado.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Iniciar sesión", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    Text(
                        "  o  ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }

                OutlinedButton(
                    onClick = onIrACrearCuenta,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Crear cuenta", style = MaterialTheme.typography.titleMedium)
                }
            }
        )
    }
}

