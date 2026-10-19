package com.example.proyectorinconsur.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectorinconsur.data.entity.TurnoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TurnoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(turno: TurnoEntity): Long

    @Query("SELECT * FROM turnos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): TurnoEntity?

    @Query("SELECT * FROM turnos WHERE usuarioId = :usuarioId AND estado = 'ACTIVO' LIMIT 1")
    fun observarActivoDeUsuario(usuarioId: Long): Flow<TurnoEntity?>

    @Query("SELECT * FROM turnos WHERE usuarioId = :usuarioId AND estado = 'ACTIVO' LIMIT 1")
    suspend fun obtenerActivoDeUsuario(usuarioId: Long): TurnoEntity?

    // Devuelve cuántas filas cambió (Int); solo cierra turnos que siguen activos
    @Query("UPDATE turnos SET estado = 'CERRADO', horaFin = :horaFin WHERE id = :id AND estado = 'ACTIVO'")
    suspend fun cerrar(id: Long, horaFin: Long): Int

    @Query("UPDATE turnos SET foto = :uri WHERE id = :id")
    suspend fun actualizarFoto(id: Long, uri: String?): Int

}
