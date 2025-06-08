package com.example.numbergeneratorapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NumberGeneratorScreen(viewModel: NumberViewModel = viewModel()) {
    val uiState = viewModel.uiState
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        when(uiState){
            is NumberUiState.Idle -> {
                Button(onClick = { viewModel.generateNumber() }) {
                    Text("Gerar numero aleatorio")
                }
            }

            is NumberUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp)))
                Text("Gerando numero aleatorio...")
            }
            is NumberUiState.Success -> {
                Text(
                    "Numero gerado: ${(uiState as NumberUiState.Success).number}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.reset() }) {
                    Text("Gerar novamente")
                }
            }

            is NumberUiState.Error -> {
                Text(
                    (uiState as NumberUiState.Error).message,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.reset() }) {
                    Text("Tentar novamente")
                }
            }


        }
    }
}