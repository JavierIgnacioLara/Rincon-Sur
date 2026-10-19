package com.example.proyectorinconsur.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectorinconsur.data.dao.RegistroDao
import com.example.proyectorinconsur.data.dao.TurnoDao
import com.example.proyectorinconsur.data.dao.UsuarioDao
import com.example.proyectorinconsur.data.entity.RegistroEntity
import com.example.proyectorinconsur.data.entity.TurnoEntity
import com.example.proyectorinconsur.data.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, TurnoEntity::class, RegistroEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun turnoDao(): TurnoDao
    abstract fun registroDao(): RegistroDao

    companion object {
        @Volatile private var INSTANCIA: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rinconsur.db"
                )
                    .build()
                    .also { INSTANCIA = it }
            }
    }
}
