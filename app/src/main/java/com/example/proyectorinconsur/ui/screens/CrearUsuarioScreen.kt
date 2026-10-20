package com.example.proyectorinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import com.example.proyectorinconsur.ui.components.CajaInformativa
import com.example.proyectorinconsur.ui.components.CampoTexto
import com.example.proyectorinconsur.ui.layouts.ContenedorAdaptable
import com.example.proyectorinconsur.viewmodel.CrearUsuarioViewModel
import com.example.proyectorinconsur.ui.components.MensajeError
import com.example.proyectorinconsur.ui.components.MensajeExito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(
    viewModel: CrearUsuarioViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current


    LaunchedEffect(estado.cuentaCreada) {
        if (estado.cuentaCreada) {
            delay(1600)
            viewModel.consumirCuentaCreada()
            onVolver()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Crear usuario", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "Registra un nuevo usuario",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        ContenedorAdaptable(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            espacio = 14.dp,
            izquierda = {
                if (estado.esPrimerUsuario) {
                    CajaInformativa("Aún no hay usuarios: la primera cuenta que crees será la del administrador.")
                }

                CampoTexto(
                    valor = estado.nombre,
                    onCambio = viewModel::onNombreChange,
                    etiqueta = "Nombre completo",
                    icono = Icons.Default.Person,
                    error = estado.errores.nombre
                )

                CampoTexto(
                    valor = estado.correo,
                    onCambio = viewModel::onCorreoChange,
                    etiqueta = "Correo electrónico",
                    icono = Icons.Default.Email,
                    error = estado.errores.correo,
                    tipoTeclado = KeyboardType.Email
                )
            },
            derecha = {
                CampoTexto(
                    valor = estado.clave,
                    onCambio = viewModel::onClaveChange,
                    etiqueta = "Contraseña",
                    icono = Icons.Default.Lock,
                    error = estado.errores.clave,
                    esClave = true
                )

                Text(
                    "La contraseña debe tener mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo, sin espacios.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                CampoTexto(
                    valor = estado.confirmacion,
                    onCambio = viewModel::onConfirmacionChange,
                    etiqueta = "Confirmar contraseña",
                    icono = Icons.Default.Lock,
                    error = estado.errores.confirmacion,
                    esClave = true,
                    accionIme = ImeAction.Done,
                    alAccionIme = {
                        foco.clearFocus()
                        viewModel.crearCuenta()
                    }
                )

                MensajeError(estado.errorMsg)
                MensajeExito(visible = estado.cuentaCreada, texto = "Cuenta creada correctamente. Ya puedes iniciar sesión.")

                Button(
                    onClick = {
                        foco.clearFocus()
                        viewModel.crearCuenta()
                    },
                    enabled = !estado.isSubmitting && !estado.cuentaCreada,
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
                        Text("Crear cuenta", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "¿Ya tienes una cuenta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onVolver) { Text("Volver al inicio de sesión") }
                }
            }
        )
    }
}

