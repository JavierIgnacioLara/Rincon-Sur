package com.example.proyectorinconsur.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.proyectorinconsur.model.Turno
import com.example.proyectorinconsur.data.dao.TurnoDao
import com.example.proyectorinconsur.data.entity.TurnoEntity
import com.example.proyectorinconsur.utils.inicioDelDia
import com.example.proyectorinconsur.data.entity.toDomain

sealed class ResultadoTurno {
    data class Exito(val turno: Turno) : ResultadoTurno()
    data object YaHayTurnoActivo : ResultadoTurno()
    data class Error(val mensaje: String) : ResultadoTurno()
}

class TurnoRepository(private val dao: TurnoDao) {

    fun observarActivo(usuarioId: Long): Flow<Turno?> =
        dao.observarActivoDeUsuario(usuarioId).map { it?.toDomain() }


    suspend fun iniciarTurno(
        usuarioId: Long,
        foto: String?
    ): ResultadoTurno {
        return try {
            if (dao.obtenerActivoDeUsuario(usuarioId) != null) {
                return ResultadoTurno.YaHayTurnoActivo
            }
            val ahora = System.currentTimeMillis()
            val entidad = TurnoEntity(
                usuarioId = usuarioId,
                fecha = inicioDelDia(ahora),
                horaInicio = ahora,
                foto = foto
            )
            ResultadoTurno.Exito(entidad.copy(id = dao.insertar(entidad)).toDomain())
        } catch (e: Exception) {
            ResultadoTurno.Error("No se pudo iniciar el turno. Inténtalo nuevamente.")
        }
    }


    suspend fun cerrarTurno(turnoId: Long): Boolean =
        try { dao.cerrar(turnoId, System.currentTimeMillis()) > 0 } catch (e: Exception) { false }

    suspend fun actualizarFoto(turnoId: Long, uri: String?): Boolean =
        try { dao.actualizarFoto(turnoId, uri) > 0 } catch (e: Exception) { false }

}
