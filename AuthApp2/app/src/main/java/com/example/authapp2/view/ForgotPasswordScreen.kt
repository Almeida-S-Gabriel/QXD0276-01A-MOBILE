package com.example.authapp2.view


import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authapp2.viewmodel.AuthViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(viewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Estado derivado: A validade do email é calculada a partir do estado 'email'.
    // Isso é mais eficiente do que ter um estado separado para a validação.
    val isEmailValid by remember(email) {
        derivedStateOf {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    // O botão só é habilitado se não estiver carregando e o email for válido.
    val isButtonEnabled = !isLoading && isEmailValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp), // Um pouco mais de padding lateral
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho
        Text(
            text = "Esqueceu sua senha?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sem problemas! Digite seu email e enviaremos um link para você.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Seu email de cadastro") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Ícone de Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = email.isNotEmpty() && !isEmailValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        // Mensagem de erro que aparece e desaparece suavemente
        AnimatedVisibility(visible = email.isNotEmpty() && !isEmailValid) {
            Text(
                text = "Por favor, insira um email válido.",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Recuperação
        Button(
            onClick = {
                // A lógica de validação já está no 'enabled' do botão
                isLoading = true
                focusManager.clearFocus() // Esconde o teclado
                viewModel.resetPassword(email) { success ->
                    isLoading = false // Para de carregar, independentemente do resultado
                    if (success) {
                        Toast.makeText(context, "Email de recuperação enviado!", Toast.LENGTH_LONG).show()
                        navController.popBackStack() // Volta para a tela anterior (login)
                    } else {
                        Toast.makeText(context, "Falha ao enviar o email. Tente novamente.", Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            // Mostra o indicador de progresso ou o texto, dependendo do estado 'isLoading'
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Enviar Link", fontSize = 16.sp)
            }
        }

        // Botão para Voltar ao Login
        TextButton(
            onClick = { if (!isLoading) navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Lembrei a senha! Voltar")
        }
    }
}