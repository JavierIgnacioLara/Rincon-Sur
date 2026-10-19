package com.example.proyectorinconsur.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectorinconsur.data.entity.RegistroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(registro: RegistroEntity): Long

    @Query("SELECT COUNT(*) FROM registros WHERE turnoId = :turnoId")
    fun contarPorTurno(turnoId: Long): Flow<Int>

    @Query("SELECT * FROM registros WHERE turnoId = :turnoId ORDER BY fechaHora DESC")
    fun observarPorTurno(turnoId: Long): Flow<List<RegistroEntity>>

    @Query("SELECT * FROM registros WHERE fechaHora >= :desde AND fechaHora < :hasta ORDER BY fechaHora DESC")
    fun observarPorRango(desde: Long, hasta: Long): Flow<List<RegistroEntity>>
}