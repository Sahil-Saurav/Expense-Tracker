package com.example.expensetracker

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationScreen() {
    val navController:NavHostController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home"){
            HomeScreen(navController = navController)
        }
        composable("add"){
            AddExpense(navController = navController)
        }
    }
}