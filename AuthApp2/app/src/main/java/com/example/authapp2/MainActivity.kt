package com.example.authapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp2.data.AuthRepository
import com.example.authapp2.ui.theme.AuthApp2Theme // <-- Importante: Use o nome do seu tema
import com.example.authapp2.ui.view.LoginScreen
import com.example.authapp2.view.ForgotPasswordScreen
import com.example.authapp2.view.HomeScreen
import com.example.authapp2.view.RegisterScreen
import com.example.authapp2.viewmodel.AuthViewModel
import com.example.authapp2.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    // Maneira moderna e recomendada de instanciar um ViewModel com factory
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. O aplicativo inteiro deve estar dentro do seu tema
            AuthApp2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // 2. O NavHost define todas as rotas de navegação possíveis
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(authViewModel, navController)
                        }
                        composable("register") {
                            RegisterScreen(authViewModel, navController)
                        }
                        composable("forgotPassword") {
                            ForgotPasswordScreen(authViewModel, navController)
                        }
                        composable("home") {
                            HomeScreen(authViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}