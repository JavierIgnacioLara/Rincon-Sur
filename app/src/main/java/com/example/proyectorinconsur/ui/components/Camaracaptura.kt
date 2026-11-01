package com.example.proyectorinconsur.ui.components

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.proyectorinconsur.utils.crearArchivoFoto
import com.example.proyectorinconsur.utils.uriDeArchivo

@Composable
fun CamaraCaptura(
    onFotoTomada: (Uri) -> Unit,
    onCancelar: () -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val imageCapture = remember { ImageCapture.Builder().build() }
    val previewView = remember { PreviewView(context) }
    var capturando by remember { mutableStateOf(false) }


    DisposableEffect(lifecycleOwner) {
        val futuroProveedor = ProcessCameraProvider.getInstance(context)
        futuroProveedor.addListener({
            try {
                val proveedor = futuroProveedor.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                proveedor.unbindAll()
                proveedor.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                onError("No se pudo iniciar la cámara.")
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            try {
                futuroProveedor.get().unbindAll()
            } catch (e: Exception) {
                // la cámara ya estaba liberada
            }
        }
    }

    fun tomarFoto() {
        if (capturando) return
        capturando = true
        val archivo = crearArchivoFoto(context)
        val opciones = ImageCapture.OutputFileOptions.Builder(archivo).build()
        imageCapture.takePicture(
            opciones,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(resultado: ImageCapture.OutputFileResults) {
                    capturando = false
                    onFotoTomada(uriDeArchivo(context, archivo))
                }

                override fun onError(excepcion: ImageCaptureException) {
                    capturando = false
                    onError("No se pudo tomar la foto. Inténtalo nuevamente.")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        IconButton(
            onClick = onCancelar,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.White)
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            FilledIconButton(
                onClick = ::tomarFoto,
                enabled = !capturando,
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White),
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = "Tomar foto",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}