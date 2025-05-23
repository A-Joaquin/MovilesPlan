package com.lainus.examen.navigation

sealed class AppRoutes(val route: String) {
    object Plans : AppRoutes("plans")
}