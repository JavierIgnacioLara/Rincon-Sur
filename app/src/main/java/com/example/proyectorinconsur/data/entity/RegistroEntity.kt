package com.example.proyectorinconsur.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registros",
    foreignKeys = [ForeignKey(
        entity = UsuarioEntity::class,
        parentColumns = ["id"],
        childColumns = ["turnoId"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index("turnoId"), Index("aceesoEntradaId"), Index("fechaHora")]
)

data class RegistroEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val turnoId: Long,
    val fechaHora: Long,
    val accesoEntradaId: Int,
    val accesoSalidaId: Int,
    val generoId: Int,
    val rangoEtarioId: Int,
    val tipoRegistroId: Int,
    val acompAdultos: Int = 0,
    val acompNinos: Int = 0
)