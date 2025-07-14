package com.example.msgapp.view
import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.msgapp.model.Message
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@Composable
fun ChatScreen(
    username: String,
    userId: String,
    messages: List<Message>,
    onSend: (String) -> Unit,
    currentRoom: String,
    lastNotifiedId: String?,
    onNotify: (Message) -> Unit,
    onLeaveRoom: (() -> Unit)? = null // Mantido, mas não usado na estrutura original
) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(messages.size) {
        val lastMsg = messages.lastOrNull()
        if (lastMsg != null && lastMsg.senderId != userId && lastMsg.id != lastNotifiedId) {
            onNotify(lastMsg)
        }
    }

    Column(Modifier.fillMaxSize()) {
        // Cabeçalho da sala (agora é parte da ChatScreen, como no original)
        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sala: $currentRoom",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                // O botão "Trocar sala" não estava no layout original, mas mantendo o parâmetro
                if (onLeaveRoom != null) {
                    TextButton(onClick = { onLeaveRoom() }) {
                        Text("Trocar sala")
                    }
                }
            }
        }

        // Mensagens
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(
                    msg = msg,
                    isOwn = msg.senderId == userId
                )
            }
        }

        // Área de digitação
        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Digite sua mensagem...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (input.isNotBlank()) {
                            onSend(input)
                            input = ""
                        }
                    },
                    shape = CircleShape,
                    enabled = input.isNotBlank(),
                    modifier = Modifier.size(52.dp)
                ) {
                    Text("➤", style = MaterialTheme.typography.titleMedium) // Ícone de Enviar como texto
                }
            }
        }
    }
}


@Composable
fun MessageBubble(msg: Message, isOwn: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isOwn) 56.dp else 0.dp,
                end = if (isOwn) 0.dp else 56.dp,
            ),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isOwn) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = if (isOwn) 4.dp else 16.dp,
                bottomStart = if (isOwn) 16.dp else 4.dp
            ),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Nome do remetente para mensagens recebidas
                if (!isOwn) {
                    Text(
                        text = msg.senderName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Texto da mensagem
                Text(
                    text = msg.texto,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isOwn) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                )

                // Horário
                Text(
                    text = android.text.format.DateFormat.format("HH:mm", msg.timestamp).toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (isOwn) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer).copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }
    }
}

// Notificação local
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun notifyNewMessage(context: Context, message: Message) {
    val channelId = "chat_messages"
    val notificationManager = NotificationManagerCompat.from(context)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId, "Mensagens", NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Nova mensagem de ${message.senderName}")
        .setContentText(message.texto)
        .setSmallIcon(R.drawable.ic_dialog_email)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
    notificationManager.notify(message.id.hashCode(), notification)
}