package com.example.proyectorinconsur.data.entity

import com.example.proyectorinconsur.model.Registro
import com.example.proyectorinconsur.model.Turno
import com.example.proyectorinconsur.model.Usuario


fun UsuarioEntity.toDomain() = Usuario(
    id = id, nombre = nombre, correo = correo, rol = rol, creadoEn = creadoEn
)

fun TurnoEntity.toDomain() = Turno(
    id = id, usuarioId = usuarioId, fecha = fecha, horaInicio = horaInicio, horaFin = horaFin,
    estado = estado, fotoUri = foto
)

fun RegistroEntity.toDomain() = Registro(
    id = id, turnoId = turnoId, fechaHora = fechaHora,
    accesoEntradaId = accesoEntradaId, accesoSalidaId = accesoSalidaId,
    generoId = generoId, rangoEtarioId = rangoEtarioId, tipoRegistroId = tipoRegistroId,
    acompAdultos = acompAdultos, acompNinos = acompNinos
)