package com.example.investidorapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investidorapp.model.Investimento
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.investidorapp.viewmodel.build.InvestimentosViewModel

private val PositiveGreen = Color(0xFF2E7D32)
private val LightBackground = Color(0xFFF3F9F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestidorScreen(viewModel: InvestimentosViewModel) {
    val investimentos by viewModel.investimentos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Investidor App") },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PositiveGreen,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PositiveGreen,
                        strokeWidth = 6.dp
                    )
                }
                investimentos.isEmpty() -> {
                    Text(
                        text = "Nenhum investimento encontrado.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    ListaInvestimentos(investimentos)
                }
            }
        }
    }
}

@Composable
fun ListaInvestimentos(investimentos: List<Investimento>) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(investimentos) { investimento ->
            InvestimentoItem(investimento)
        }
    }
}

@Composable
fun InvestimentoItem(investimento: Investimento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFE8F5E9),
                            Color(0xFFB9F6CA)
                        )
                    )
                )
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(PositiveGreen.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.TrendingUp,
                    contentDescription = "Ícone de investimento",
                    tint = PositiveGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = investimento.nome,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = PositiveGreen
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Valor: R$ ${investimento.valor}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    color = PositiveGreen.copy(alpha = 0.85f)
                )
            }
        }
    }
}
