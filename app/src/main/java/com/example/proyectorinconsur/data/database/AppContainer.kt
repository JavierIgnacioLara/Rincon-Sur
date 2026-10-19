package com.example.proyectorinconsur.data.database

import android.content.Context
import com.example.proyectorinconsur.data.entity.UserPreferences
import com.example.proyectorinconsur.data.repository.RegistroRepository
import com.example.proyectorinconsur.data.repository.TurnoRepository
import com.example.proyectorinconsur.data.repository.UsuarioRepository
import com.example.proyectorinconsur.data.session.FiltroHistorial
import com.example.proyectorinconsur.data.session.SessionManager
import com.example.proyectorinconsur.model.Turno
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/** Crea una sola vez la base de datos, los repositorios y la sesión (sobrevive a la rotación). */
class AppContainer private constructor(context: Context) {
    private val db = AppDatabase.obtener(context)
    private val preferencias = UserPreferences(context.applicationContext)

    val usuarioRepository = UsuarioRepository(db.usuarioDao())
    val turnoRepository = TurnoRepository(db.turnoDao())
    val registroRepository = RegistroRepository(db.registroDao(), db.turnoDao())
    val sesion = SessionManager(preferencias, usuarioRepository)
    val filtroHistorial = FiltroHistorial()

    /** Turno activo del usuario con sesión iniciada (null si no hay sesión o no hay turno activo). */
    @OptIn(ExperimentalCoroutinesApi::class)
    val turnoActivo: Flow<Turno?> = sesion.usuarioId.flatMapLatest { id ->
        if (id == null) flowOf(null) else turnoRepository.observarActivo(id)
    }

    companion object {
        @Volatile private var instancia: AppContainer? = null

        fun obtener(context: Context): AppContainer =
            instancia ?: synchronized(this) {
                instancia ?: AppContainer(context.applicationContext).also { instancia = it }
            }
    }
}