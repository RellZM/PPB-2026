package com.example.kuis_01

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: StudentViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("home") {
            StudentListScreen(
                viewModel = viewModel, 
                onNavigateToAdd = {
                    navController.navigate("add")
                }, 
                onNavigateToEdit = { studentId ->
                    navController.navigate("edit/$studentId")
                }
            )
        }
        composable("add") {
            StudentFormScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("edit/{studentId}") { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId")
            StudentFormScreen(
                viewModel = viewModel,
                studentId = studentId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
