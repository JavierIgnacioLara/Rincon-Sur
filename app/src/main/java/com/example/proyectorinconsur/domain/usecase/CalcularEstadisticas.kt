package com.example.proyectorinconsur.domain.usecase

import com.example.proyectorinconsur.domain.model.ClasificacionRegistro
import com.example.proyectorinconsur.domain.model.Estadisticas
import com.example.proyectorinconsur.domain.model.GENERO_HOMBRE
import com.example.proyectorinconsur.domain.model.GENERO_MUJER
import com.example.proyectorinconsur.domain.model.RANGO_ADULTO
import com.example.proyectorinconsur.domain.model.RANGO_MAYOR
import com.example.proyectorinconsur.domain.model.RANGO_NINO
import com.example.proyectorinconsur.domain.model.TIPO_COMPRO
import com.example.proyectorinconsur.domain.model.TIPO_TRANSITO


fun calcularEstadisticas(registros: List<ClasificacionRegistro>): Estadisticas = Estadisticas(
    total = registros.size,
    transito = registros.count { it.tipoRegistroId == TIPO_TRANSITO },
    compro = registros.count { it.tipoRegistroId == TIPO_COMPRO },
    hombres = registros.count { it.generoId == GENERO_HOMBRE },
    mujeres = registros.count { it.generoId == GENERO_MUJER },
    ninos = registros.count { it.rangoEtarioId == RANGO_NINO },
    adultos = registros.count { it.rangoEtarioId == RANGO_ADULTO },
    mayores = registros.count { it.rangoEtarioId == RANGO_MAYOR },
    acompTransitoAdultos = registros.filter { it.tipoRegistroId == TIPO_TRANSITO }.sumOf { it.acompAdultos },
    acompTransitoNinos = registros.filter { it.tipoRegistroId == TIPO_TRANSITO }.sumOf { it.acompNinos },
    acompCompraAdultos = registros.filter { it.tipoRegistroId == TIPO_COMPRO }.sumOf { it.acompAdultos },
    acompCompraNinos = registros.filter { it.tipoRegistroId == TIPO_COMPRO }.sumOf { it.acompNinos }
)

fun <T : ClasificacionRegistro> List<T>.deAcceso(accesoId: Int): List<T> =
    filter { it.accesoEntradaId == accesoId }