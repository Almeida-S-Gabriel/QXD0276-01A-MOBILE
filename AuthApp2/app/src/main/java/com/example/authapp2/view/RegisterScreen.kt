
package com.example.authapp2.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authapp2.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(viewModel: AuthViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Validação em tempo real para os campos
    val isNameValid by remember(name) { derivedStateOf { name.isNotBlank() } }
    val isEmailValid by remember(email) { derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() } }
    val isPasswordValid by remember(password) { derivedStateOf { password.length >= 6 } }

    // O formulário só é válido se todos os campos forem válidos
    val isFormValid by remember(isNameValid, isEmailValid, isPasswordValid) {
        derivedStateOf { isNameValid && isEmailValid && isPasswordValid }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Criar Conta", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de Nome com navegação por teclado
                    CustomTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Nome",
                        leadingIcon = Icons.Filled.Person,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo de Email com navegação por teclado
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo de Senha com visualizador e ação de "Done" no teclado
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Senha (mín. 6 caracteres)",
                        leadingIcon = Icons.Filled.Lock,
                        isPasswordTextField = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Botão de Registrar com estado de carregamento
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            isLoading = true
                            viewModel.register(email, password, name) { success ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack() // Volta para a tela de login
                                } else {
                                    Toast.makeText(context, "Erro no cadastro. Verifique os dados.", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        enabled = isFormValid && !isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Registrar", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { if (!isLoading) navController.popBackStack() },
                        enabled = !isLoading
                    ) {
                        Text("Já tem uma conta? Faça login")
                    }
                }
            }
        }
    }
}

