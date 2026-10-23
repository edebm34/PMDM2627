package io.github.edebm34.login

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ---------------------------------------------------------
// Definimos las rutas como constantes para evitar errores tipográficos.
// ---------------------------------------------------------
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home/{email}"

    fun home(email: String): String = "home/${Uri.encode(email)}"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // --- LOGIN ---
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { email ->
                    navController.navigate(Routes.home(email)) {
                        // Evita que al volver atrás se vuelva al login
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // --- REGISTRO ---
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { email ->
                    navController.navigate(Routes.home(email)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // --- HOME (con argumento email) ---
        composable(
            route = Routes.HOME,
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            HomeScreen(
                userEmail = email,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}