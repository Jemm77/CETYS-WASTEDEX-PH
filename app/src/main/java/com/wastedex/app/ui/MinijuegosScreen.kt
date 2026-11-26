package com.wastedex.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wastedex.app.CategoriesStore

/** MinijuegosHome.swift: encabezado + 3 tarjetas con puntuación máxima. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinijuegosScreen(store: CategoriesStore, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    var juegoAbierto by remember { mutableStateOf<Int?>(null) }
    var scores by remember { mutableStateOf(listOf(1, 2, 3).map { store.highScore(it) }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minijuegos") },
                actions = { TextButton(onClick = onBack) { Text("Cerrar", fontWeight = FontWeight.Bold) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier.background(
            Brush.verticalGradient(listOf(Color(0xFFD4F5E0), Color(0xFFC2E6FA)))
        )
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF26BD75), Color(0xFF0A9ECC))),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Zona de Minijuegos", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    "Aprende reciclando con retos rápidos y divertidos.",
                    color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp, fontWeight = FontWeight.SemiBold
                )
            }

            MinijuegoCard(
                "Minijuego 1", "Clasifica residuos en su lugar", Icons.Filled.Inventory2,
                listOf(Color(0xFF30C771), Color(0xFF17A1EB)), scores[0]
            ) { juegoAbierto = 1 }
            MinijuegoCard(
                "Minijuego 2", "Adivina fotos de tu WasteDex", Icons.Filled.PhotoLibrary,
                listOf(Color(0xFFFCA838), Color(0xFFF76B47)), scores[1]
            ) { juegoAbierto = 2 }
            MinijuegoCard(
                "Minijuego 3", "Memorama de reciclaje", Icons.Filled.AutoAwesome,
                listOf(Color(0xFF7D85F7), Color(0xFF42BAE6)), scores[2]
            ) { juegoAbierto = 3 }
        }
    }

    // Placeholder: aquí irían Minijuego1/2/3 (no incluidos, solo vistas principales).
    juegoAbierto?.let { n ->
        AlertDialog(
            onDismissRequest = { juegoAbierto = null; scores = listOf(1, 2, 3).map { store.highScore(it) } },
            confirmButton = {
                TextButton(onClick = { juegoAbierto = null; scores = listOf(1, 2, 3).map { store.highScore(it) } }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Minijuego $n") },
            text = { Text("Pantalla del juego pendiente de portar.") }
        )
    }
}

@Composable
private fun MinijuegoCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    colors: List<Color>,
    bestScore: Int,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Surface(
        onClick = onClick,
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth().shadow(8.dp, shape)
    ) {
        Row(
            Modifier
                .background(Brush.linearGradient(colors))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                Modifier.size(54.dp).background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, null, tint = Color.White, modifier = Modifier.size(28.dp)) }

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text(subtitle, color = Color.White.copy(alpha = 0.92f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Puntuación máxima: $bestScore", color = Color.White.copy(alpha = 0.95f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Filled.ChevronRight, null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
    }
}
