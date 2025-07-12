package com.example.investidorapp.viewmodel.build

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.investidorapp.MainActivity
import com.example.investidorapp.R
import com.example.investidorapp.model.Investimento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InvestimentosViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseDatabase.getInstance().reference.child("Investimentos")

    private val _investimentos = MutableStateFlow<List<Investimento>>(emptyList())
    val investimentos: StateFlow<List<Investimento>> = _investimentos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        carregarInvestimentos()
        monitorarAlteracoes()
    }

    private fun monitorarAlteracoes() {
        db.addChildEventListener(object : ChildEventListener {
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val nome = snapshot.child("nome").getValue(String::class.java) ?: "Desconhecido"
                val valor = snapshot.child("valor").getValue(Int::class.java) ?: 0
                enviarNotificacao("Investimento atualizado", "$nome agora vale R$$valor")
                carregarInvestimentos()
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                carregarInvestimentos()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                carregarInvestimentos()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = "Erro ao carregar investimentos: ${error.message}"
            }
        })
    }

    private fun carregarInvestimentos() {
        _isLoading.value = true
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Investimento>()
                for (item in snapshot.children) {
                    val nome = item.child("nome").getValue(String::class.java) ?: "Desconhecido"
                    val valor = item.child("valor").getValue(Int::class.java) ?: 0
                    lista.add(Investimento(nome, valor))
                }
                _investimentos.value = lista
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = "Erro ao carregar investimentos: ${error.message}"
                _isLoading.value = false
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    private fun enviarNotificacao(titulo: String, mensagem: String) {
        val context = getApplication<Application>()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissão não concedida, não tentar enviar notificação
            return
        }

        // restante do seu código de notificação
        val channelId = "investimentos_notifications"
        val notificationId = (System.currentTimeMillis() % 10000).toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificações de Investimentos",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
