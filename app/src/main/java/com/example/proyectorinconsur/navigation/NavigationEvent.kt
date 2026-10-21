package com.example.proyectorinconsur.navigation

sealed class NavigationEvent {
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    data class IrAPestana(val destino: Screen) : NavigationEvent()

    data object PopBackStack : NavigationEvent()
}