package com.example.proyectorinconsur.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectorinconsur.data.database.AppContainer
import com.example.proyectorinconsur.ui.screens.AccesosScreen
import com.example.proyectorinconsur.ui.screens.CrearUsuarioScreen
import com.example.proyectorinconsur.ui.screens.DetalleAccesoScreen
import com.example.proyectorinconsur.ui.screens.HistorialScreen
import com.example.proyectorinconsur.ui.screens.InicioScreen
import com.example.proyectorinconsur.ui.screens.LoginScreen
import com.example.proyectorinconsur.ui.screens.RegistrarPersonaScreen
import com.example.proyectorinconsur.viewmodel.AccesosViewModel
import com.example.proyectorinconsur.viewmodel.CrearUsuarioViewModel
import com.example.proyectorinconsur.viewmodel.DetalleAccesoViewModel
import com.example.proyectorinconsur.viewmodel.HistorialViewModel
import com.example.proyectorinconsur.viewmodel.InicioViewModel
import com.example.proyectorinconsur.viewmodel.LoginViewModel
import com.example.proyectorinconsur.viewmodel.MainViewModel
import com.example.proyectorinconsur.viewmodel.RegistrarPersonaViewModel
import com.example.proyectorinconsur.viewmodel.VmFactory

private const val DURACION_TRANSICION = 280

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel,
    container: AppContainer
) {
    val irAPestana: (Screen) -> Unit = { mainViewModel.irAPestana(it) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(tween(DURACION_TRANSICION)) + slideInHorizontally(tween(DURACION_TRANSICION)) { it / 8 }
        },
        exitTransition = { fadeOut(tween(DURACION_TRANSICION / 2)) },
        popEnterTransition = { fadeIn(tween(DURACION_TRANSICION)) },
        popExitTransition = {
            fadeOut(tween(DURACION_TRANSICION)) + slideOutHorizontally(tween(DURACION_TRANSICION)) { it / 8 }
        }
    ) {
        composable(Screen.Login.route) {
            val vm: LoginViewModel = viewModel(
                factory = VmFactory { LoginViewModel(container.usuarioRepository, container.sesion) }
            )
            LoginScreen(
                viewModel = vm,
                onIrACrearCuenta = { mainViewModel.navigateTo(Screen.CrearUsuario) },
                onLoginExitoso = {
                    mainViewModel.navigateTo(Screen.Inicio, popUpTo = Screen.Login, inclusive = true)
                }
            )
        }

        composable(Screen.CrearUsuario.route) {
            val vm: CrearUsuarioViewModel = viewModel(
                factory = VmFactory { CrearUsuarioViewModel(container.usuarioRepository) }
            )
            CrearUsuarioScreen(viewModel = vm, onVolver = { mainViewModel.navigateBack() })
        }

        composable(Screen.Inicio.route) {
            val vm: InicioViewModel = viewModel(
                factory = VmFactory {
                    InicioViewModel(
                        container.usuarioRepository,
                        container.sesion,
                        container.turnoRepository,
                        container.turnoActivo
                    )
                }
            )
            InicioScreen(
                viewModel = vm,
                onIrARegistrar = { mainViewModel.navigateTo(Screen.RegistrarPersona) },
                onSeleccionarPestana = irAPestana,
                onSesionCerrada = {
                    mainViewModel.navigateTo(Screen.Login, popUpTo = Screen.Inicio, inclusive = true)
                }
            )
        }

        composable(Screen.RegistrarPersona.route) {
            val vm: RegistrarPersonaViewModel = viewModel(
                factory = VmFactory {
                    RegistrarPersonaViewModel(container.registroRepository, container.turnoActivo)
                }
            )
            RegistrarPersonaScreen(
                viewModel = vm,
                onVolver = { mainViewModel.navigateBack() },
                onIrAInicio = { mainViewModel.irAPestana(Screen.Inicio) },
                onSeleccionarPestana = irAPestana
            )
        }

        composable(Screen.Accesos.route) {
            val vm: AccesosViewModel = viewModel(
                factory = VmFactory { AccesosViewModel(container.registroRepository, container.turnoActivo) }
            )
            AccesosScreen(
                viewModel = vm,
                onVerDetalle = { accesoId -> mainViewModel.navigateTo(Screen.DetalleAcceso(accesoId)) },
                onSeleccionarPestana = irAPestana
            )
        }

        composable(
            route = RUTA_DETALLE_ACCESO,
            arguments = listOf(navArgument("accesoId") { type = NavType.IntType })
        ) { entrada ->
            val accesoId = entrada.arguments?.getInt("accesoId") ?: 1
            val vm: DetalleAccesoViewModel = viewModel(
                factory = VmFactory {
                    DetalleAccesoViewModel(
                        accesoId,
                        container.filtroHistorial,
                        container.registroRepository,
                        container.turnoActivo
                    )
                }
            )
            DetalleAccesoScreen(
                accesoId = accesoId,
                viewModel = vm,
                onVolver = { mainViewModel.navigateBack() },
                onVerHistorial = { mainViewModel.irAPestana(Screen.Historial) },
                onSeleccionarPestana = irAPestana
            )
        }

        composable(Screen.Historial.route) {
            val vm: HistorialViewModel = viewModel(
                factory = VmFactory { HistorialViewModel(container.filtroHistorial, container.registroRepository) }
            )
            HistorialScreen(viewModel = vm, onSeleccionarPestana = irAPestana)
        }
    }
}