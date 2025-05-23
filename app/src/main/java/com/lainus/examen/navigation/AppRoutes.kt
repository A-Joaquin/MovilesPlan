package com.lainus.examen.navigation

sealed class AppRoutes(val route: String) {
    object Plans : AppRoutes("plans")
    object Shipping : AppRoutes("shipping_screen")

    // DEBUG: Agregar init para verificar las rutas
    init {
        println("DEBUG: AppRoutes creado - route: $route")
    }
}