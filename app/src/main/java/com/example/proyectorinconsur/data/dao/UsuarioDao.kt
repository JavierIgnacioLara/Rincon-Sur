package com.example.proyectorinconsur.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectorinconsur.data.entity.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): UsuarioEntity?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
