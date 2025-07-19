package com.example.authapp2.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authapp2.viewmodel.AuthViewModel
@Composable
fun HomeScreen(viewModel: AuthViewModel, navController: NavController) {
    var userName by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Efeito para buscar o nome do usuário uma única vez quando a tela é exibida
    LaunchedEffect(Unit) {
        viewModel.getUserName { name ->
            userName = name ?: "Usuário" // Atualiza o nome do usuário
            isLoading = false           // Finaliza o estado de carregamento
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Mostra o indicador de progresso enquanto 'isLoading' for verdadeiro
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Mostra o conteúdo principal quando o carregamento termina
            HomeScreenContent(
                userName = userName ?: "Usuário",
                onLogout = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(userName: String, onLogout: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Ícone de Usuário",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bem-vindo,\n$userName!",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sair"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sair", fontSize = 18.sp)
                }
            }
        }
    }
}