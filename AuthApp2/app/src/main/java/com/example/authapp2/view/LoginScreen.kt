package com.example.authapp2.ui.view // <-- MUDANÇA AQUI

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authapp2.R
import com.example.authapp2.view.CustomTextField
import com.example.authapp2.viewmodel.AuthViewModel // <-- MUDANÇA AQUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

// Assumindo que seu CustomTextField está em um arquivo 'Components.kt'
// dentro do mesmo pacote (com.example.authapp2.ui.view)

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {
    // O resto do seu código da LoginScreen não precisa de mudanças,
    // apenas o package e os imports.
    // ... cole o restante do seu código da LoginScreen aqui ...
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isLoading = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.loginWithGoogle(idToken) { success ->
                    isLoading = false
                    if (success) {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    } else {
                        Toast.makeText(context, "Erro no login com Google", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run { isLoading = false }
        } catch (e: ApiException) {
            isLoading = false
            Toast.makeText(context, "Falha no login com Google: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    val isFormValid by remember(email, password) {
        derivedStateOf { email.isNotBlank() && password.isNotBlank() }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Login", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(24.dp))

                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Senha",
                        leadingIcon = Icons.Filled.Lock,
                        isPasswordTextField = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            isLoading = true
                            viewModel.login(email, password) { success ->
                                isLoading = false
                                if (success) {
                                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                                } else {
                                    Toast.makeText(context, "Usuário ou senha inválida", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        enabled = isFormValid && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Entrar", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            val signInIntent = viewModel.getGoogleSignInClient(context).signInIntent
                            googleSignInLauncher.launch(signInIntent)
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Image(painter = painterResource(id = R.drawable.logo_google), contentDescription = null, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Entrar com Google", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { if (!isLoading) navController.navigate("forgotPassword") },
                            enabled = !isLoading
                        ) {
                            Text("Esqueci a senha")
                        }
                        TextButton(
                            onClick = { if (!isLoading) navController.navigate("register") },
                            enabled = !isLoading
                        ) {
                            Text("Criar Conta")
                        }
                    }
                }
            }
        }
    }
}