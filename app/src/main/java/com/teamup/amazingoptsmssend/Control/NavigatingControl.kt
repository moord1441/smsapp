package com.teamup.amazingoptsmssend.Control

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teamup.amazingoptsmssend.act.Home
import com.teamup.amazingoptsmssend.act.vsms


@Composable
fun NavigatingControl(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.idScreen) {
        composable( "${Screen.VSMS.idScreen}/{index}", arguments = listOf(
        navArgument("index") { type = NavType.StringType }
        )) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")
            vsms(index,context)
        }
        composable(Screen.Home.idScreen) {
            Home(context,navController)
        }
    }
}
sealed class Screen(val  idScreen :String){
    object VSMS : Screen("VSMS")
    object Home : Screen("Home")
}
