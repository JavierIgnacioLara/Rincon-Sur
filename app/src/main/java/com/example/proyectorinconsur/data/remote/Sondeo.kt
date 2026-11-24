package com.example.proyectorinconsur.data.remote

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

/**
 * Convierte una consulta al backend en un Flow "vivo".
 *
 * Room avisaba solo cuando cambiaban los datos; con un servidor eso no existe, así que
 * la pantalla vuelve a consultar (1) cada vez que el repositorio cambia algo (el contador
 * `cambios` sube) y (2) cada `intervaloMs` por si otro dispositivo modificó datos.
 * Si falla la conexión, se conserva el último valor mostrado y se reintenta.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal fun <T> Flow<Int>.sondear(
    intervaloMs: Long = 5_000,
    obtener: suspend () -> T
): Flow<T> = flatMapLatest {
    flow {
        while (true) {
            val resultado = try {
                Result.success(obtener())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
            resultado.onSuccess { emit(it) }
            delay(intervaloMs)
        }
    }
}
