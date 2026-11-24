package com.example.proyectorinconsur.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/** Rutas relativas a la base URL (ver RetrofitClient). */
interface ApiService {

    // ---------- Auth y usuarios ----------
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): UsuarioDto

    @GET("usuarios")
    suspend fun listarUsuarios(@Query("rol") rol: String? = null): List<UsuarioDto>

    @POST("usuarios")
    suspend fun crearUsuario(@Body body: UsuarioRequestDto): UsuarioDto

    @PUT("usuarios/{id}")
    suspend fun editarUsuario(@Path("id") id: Long, @Body body: UsuarioRequestDto): UsuarioDto

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long)

    // ---------- Turnos ----------
    @POST("turnos")
    suspend fun abrirTurno(@Body body: TurnoRequestDto): TurnoDto

    @PUT("turnos/{id}/cerrar")
    suspend fun cerrarTurno(@Path("id") id: Long): TurnoDto

    @PUT("turnos/{id}/foto")
    suspend fun actualizarFoto(@Path("id") id: Long, @Body body: FotoRequestDto): TurnoDto

    /** El backend responde 204 (sin cuerpo) si no hay turno activo: por eso devuelve null. */
    @GET("turnos/activo")
    suspend fun turnoActivo(@Query("usuarioId") usuarioId: Long): TurnoDto?

    // ---------- Registros ----------
    @GET("registros")
    suspend fun listarRegistros(
        @Query("desde") desde: Long? = null,
        @Query("hasta") hasta: Long? = null,
        @Query("turnoId") turnoId: Long? = null
    ): List<RegistroDto>

    @POST("registros")
    suspend fun crearRegistro(@Body body: RegistroRequestDto): RegistroDto

    @PUT("registros/{id}")
    suspend fun editarRegistro(@Path("id") id: Long, @Body body: RegistroRequestDto): RegistroDto

    @DELETE("registros/{id}")
    suspend fun eliminarRegistro(@Path("id") id: Long)
}
