package com.example.proyectorinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectorinconsur.navigation.NavigationEvent
import com.example.proyectorinconsur.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.example.proyectorinconsur.navigation.destino

class MainViewModel : ViewModel() {
    private val _eventos = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _eventos.asSharedFlow()

    fun navigateTo(
        screen: Screen,
        popUpTo: Screen? = null,
        inclusive: Boolean = false,
        singleTop: Boolean = false
    ) {
        viewModelScope.launch {
            _eventos.emit(NavigationEvent.NavigateTo(screen, popUpTo, inclusive, singleTop))
        }
    }


    fun irAPestana(destino: Screen) {
        viewModelScope.launch { _eventos.emit(NavigationEvent.IrAPestana(destino)) }
    }

    fun navigateBack() {
        viewModelScope.launch { _eventos.emit(NavigationEvent.PopBackStack) }
    }
}