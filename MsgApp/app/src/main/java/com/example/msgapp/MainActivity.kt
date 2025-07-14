package com.example.msgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msgapp.view.ChatScreen
import com.example.msgapp.view.RoomSelector
import com.example.msgapp.viewmodel.MsgViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MsgAppTheme {
                MsgAppRoot()
            }
        }
    }
}

@Composable
fun MsgAppTheme(content: @Composable () -> Unit) {
    val softModernScheme = lightColorScheme(
        primary = Color(0xFF4F6D7A),            // Azul suave
        onPrimary = Color.White,
        primaryContainer = Color(0xFFB0C5D1),   // Azul mais claro
        onPrimaryContainer = Color(0xFF1C1C1C),

        secondary = Color(0xFFB497BD),          // Lavanda
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8D7F1), // Lavanda claro
        onSecondaryContainer = Color(0xFF2E1B33),

        tertiary = Color(0xFF4B3869),           // Roxo escuro
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFD3C4E3),
        onTertiaryContainer = Color(0xFF1F0A2A),

        error = Color(0xFFD32F2F),
        errorContainer = Color(0xFFFFDAD4),
        onError = Color.White,
        onErrorContainer = Color(0xFF410002),

        background = Color(0xFFF7F9FB),          // Cinza quase branco
        onBackground = Color(0xFF1C1C1C),
        surface = Color(0xFFF7F9FB),
        onSurface = Color(0xFF1C1C1C),
        surfaceVariant = Color(0xFFE1E6EB),      // Variante clara
        onSurfaceVariant = Color(0xFF424B50),
        outline = Color(0xFF778089)
    )

    MaterialTheme(
        colorScheme = softModernScheme,
        typography = Typography(),
        content = content
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MsgAppRoot(vm: MsgViewModel = viewModel()) {
    val context = LocalContext.current
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    val user by produceState(initialValue = firebaseAuth.currentUser) {
        if (value == null) {
            firebaseAuth.signInAnonymously()
                .addOnCompleteListener { task -> value = firebaseAuth.currentUser }
        }
    }

    val userId = user?.uid ?: "joao"
    var userName by remember { mutableStateOf("Usuário-${userId.takeLast(4)}") }
    var currentRoom by remember { mutableStateOf("geral") }
    var lastNotifiedId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentRoom) {
        vm.switchRoom(currentRoom)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensagens", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                RoomSelector(
                    onRoomSelected = { if (it.isNotBlank()) currentRoom = it },
                    modifier = Modifier.padding(12.dp)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 2.dp
            ) {
                ChatScreen(
                    username = userName,
                    userId = userId,
                    messages = vm.messages.collectAsState().value,
                    onSend = { text -> vm.sendMessage(userId, userName, text) },
                    currentRoom = currentRoom,
                    lastNotifiedId = lastNotifiedId,
                    onNotify = { msg ->
                        lastNotifiedId = msg.id
                    }
                )
            }
        }
    }
}
