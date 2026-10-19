package com.example.proyectorinconsur.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.proyectorinconsur.model.ESTADO_ACTIVO

@Entity(
    tableName = "turnos",
    foreignKeys = [ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index("usuarioId")]

)

data class TurnoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val fecha: Long,
    val horaInicio: Long,
    val horaFin: Long? = null,
    val estado: String = ESTADO_ACTIVO,
    val foto: String? = null
)
