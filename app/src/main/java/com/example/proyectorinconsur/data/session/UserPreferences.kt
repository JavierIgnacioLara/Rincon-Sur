package com.example.proyectorinconsur.data.entity

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")


class UserPreferences(private val context: Context) {

    private val usuarioRecordadoKey = longPreferencesKey("usuario_recordado_id")

    val usuarioRecordado: Flow<Long?> = context.dataStore.data.map { it[usuarioRecordadoKey] }

    suspend fun recordarUsuario(id: Long) {
        context.dataStore.edit { it[usuarioRecordadoKey] = id }
    }

    suspend fun olvidarUsuario() {
        context.dataStore.edit { it.remove(usuarioRecordadoKey) }
    }
}
