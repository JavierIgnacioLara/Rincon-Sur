package com.example.proyectorinconsur.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.proyectorinconsur.model.ESTADO_ACTIVO
import com.example.proyectorinconsur.model.Registro
import com.example.proyectorinconsur.data.dao.RegistroDao
import com.example.proyectorinconsur.data.entity.RegistroEntity
import com.example.proyectorinconsur.data.dao.TurnoDao
import com.example.proyectorinconsur.utils.finDelDia
import com.example.proyectorinconsur.utils.inicioDelDia
import com.example.proyectorinconsur.data.entity.toDomain

sealed class ResultadoRegistroPersona {
    data object Exito : ResultadoRegistroPersona()
    data object SinTurnoActivo : ResultadoRegistroPersona()
    data class Error(val mensaje: String) : ResultadoRegistroPersona()
}

class RegistroRepository(
    private val registroDao: RegistroDao,
    private val turnoDao: TurnoDao
) {
    fun contarPorTurno(turnoId: Long): Flow<Int> = registroDao.contarPorTurno(turnoId)

    fun observarPorTurno(turnoId: Long): Flow<List<Registro>> =
        registroDao.observarPorTurno(turnoId).map { lista -> lista.map { it.toDomain() } }


    fun observarPorFecha(diaMs: Long): Flow<List<Registro>> =
        registroDao.observarPorRango(inicioDelDia(diaMs), finDelDia(diaMs))
            .map { lista -> lista.map { it.toDomain() } }


    suspend fun registrar(
        turnoId: Long,
        accesoEntradaId: Int,
        accesoSalidaId: Int,
        generoId: Int,
        rangoEtarioId: Int,
        tipoRegistroId: Int,
        acompAdultos: Int = 0,
        acompNinos: Int = 0
    ): ResultadoRegistroPersona {
        return try {
            val turno = turnoDao.obtenerPorId(turnoId)
            if (turno == null || turno.estado != ESTADO_ACTIVO) {
                return ResultadoRegistroPersona.SinTurnoActivo
            }
            registroDao.insertar(
                RegistroEntity(
                    turnoId = turnoId,
                    fechaHora = System.currentTimeMillis(),
                    accesoEntradaId = accesoEntradaId,
                    accesoSalidaId = accesoSalidaId,
                    generoId = generoId,
                    rangoEtarioId = rangoEtarioId,
                    tipoRegistroId = tipoRegistroId,
                    acompAdultos = acompAdultos,
                    acompNinos = acompNinos
                )
            )
            ResultadoRegistroPersona.Exito
        } catch (e: Exception) {
            ResultadoRegistroPersona.Error("No se pudo guardar el registro. Inténtalo nuevamente.")
        }
    }
}
