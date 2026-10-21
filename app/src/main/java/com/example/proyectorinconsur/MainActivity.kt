package com.example.proyectorinconsur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.proyectorinconsur.data.database.AppContainer
import com.example.proyectorinconsur.navigation.AppNavGraph
import com.example.proyectorinconsur.navigation.NavigationEvent
import com.example.proyectorinconsur.navigation.Screen
import com.example.proyectorinconsur.navigation.destino
import com.example.proyectorinconsur.ui.theme.TemaRinconSur
import com.example.proyectorinconsur.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = AppContainer.obtener(applicationContext)

        setContent {
            TemaRinconSur {
                val mainViewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                var destinoInicial by rememberSaveable { mutableStateOf<String?>(null) }
                var sesionLista by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    container.sesion.restaurar()
                    if (destinoInicial == null) {
                        destinoInicial = if (container.sesion.usuarioId.value != null) {
                            Screen.Inicio.route
                        } else {
                            Screen.Login.route
                        }
                    }
                    sesionLista = true
                }

                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collectLatest { evento ->
                        when (evento) {
                            is NavigationEvent.NavigateTo -> navController.navigate(evento.route.destino()) {
                                evento.popUpToRoute?.let { destino ->
                                    popUpTo(destino.route) { inclusive = evento.inclusive }
                                }
                                launchSingleTop = evento.singleTop
                            }
                            is NavigationEvent.IrAPestana -> navController.navigate(evento.destino.route) {
                                popUpTo(Screen.Inicio.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                        }
                    }
                }

                val inicio = destinoInicial
                if (inicio == null || !sesionLista) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    AppNavGraph(
                        navController = navController,
                        startDestination = inicio,
                        mainViewModel = mainViewModel,
                        container = container
                    )
                }
            }
        }
    }
}