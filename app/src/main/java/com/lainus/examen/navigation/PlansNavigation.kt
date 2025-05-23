package com.lainus.examen.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.lainus.examen.plans.PlansUI
import com.lainus.examen.plans.PlansViewModel
import com.lainus.examen.shipping.ShippingScreen

@Composable
fun PlansNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Crear el ViewModel UNA SOLA VEZ a nivel de navegación
    val sharedPlansViewModel: PlansViewModel = hiltViewModel()

    println("🔥 PLANSNAVIGATION: Iniciando con AppRoutes")

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Plans.route,
        modifier = modifier
    ) {
        composable(AppRoutes.Plans.route) {
            println("🔥 PLANSNAVIGATION: Composable 'plans' ejecutándose")
            PlansUI(
                navController = navController,
                viewModel = sharedPlansViewModel
            )
        }

        composable(AppRoutes.Shipping.route) {
            println("🔥 🔥 🔥 PLANSNAVIGATION: ¡¡¡ENTRANDO A COMPOSABLE DE SHIPPING!!!")

            val selectedPlan by sharedPlansViewModel.selectedPlan.collectAsState()

            if (selectedPlan != null) {
                ShippingScreen(
                    selectedPlan = selectedPlan!!,
                    onBackClick = {
                        println("🔥 PLANSNAVIGATION: Back presionado en ShippingScreen")
                        navController.popBackStack()
                    },
                    onContinue = {
                        println("🔥 PLANSNAVIGATION: Continue presionado en ShippingScreen")
                        navController.popBackStack()
                    }
                )
            } else {
                // Si no hay plan seleccionado, mostrar loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("Cargando plan seleccionado...")
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Volver")
                        }
                    }
                }
            }
        }
    }
}