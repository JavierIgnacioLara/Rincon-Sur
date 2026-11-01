package com.example.proyectorinconsur.utils

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File


fun tienePermisoCamara(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

fun crearArchivoFoto(context: Context): File {
    val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
    return File(carpeta, "turno_${System.currentTimeMillis()}.jpg")
}
fun uriDeArchivo(context: Context, archivo: File): Uri =
    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)

fun crearUriFoto(context: Context): Uri = uriDeArchivo(context, crearArchivoFoto(context))

fun abrirAjustesDeLaApp(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {

    }
}

fun permisoCamaraDeclarado(context: Context): Boolean = try {
    val gestor = context.packageManager
    val info = if (Build.VERSION.SDK_INT >= 33) {
        gestor.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong()))
    } else {
        @Suppress("DEPRECATION")
        gestor.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
    }
    info.requestedPermissions?.contains(Manifest.permission.CAMERA) == true
} catch (e: Exception) {
    true
}

