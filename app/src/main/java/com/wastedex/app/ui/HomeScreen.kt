package com.wastedex.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wastedex.app.R

/** HomeView.swift: fondo según orientación + botones "Colección" y "Minijuegos" con pulso. */
@Composable
fun HomeScreen(onColeccion: () -> Unit, onMinijuegos: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "pulse")
    val pulse1 by infinite.animateFloat(
        1f, 1.06f,
        infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "p1"
    )
    val pulse2 by infinite.animateFloat(
        1f, 1.05f,
        infiniteRepeatable(tween(1450, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "p2"
    )

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isVertical = maxHeight > maxWidth
        Image(
            painter = painterResource(if (isVertical) R.drawable.home_vertical else R.drawable.home_horizontal),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isVertical) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.8f)
                    .widthIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                HomeButton("Colección", onColeccion, Modifier.scale(pulse1))
                HomeButton("Minijuegos", onMinijuegos, Modifier.scale(pulse2))
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = maxHeight * 0.35f, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomeButton("Colección", onColeccion, Modifier.weight(1f).scale(pulse1))
                HomeButton("Minijuegos", onMinijuegos, Modifier.weight(1f).scale(pulse2))
            }
        }
    }
}

@Composable
private fun HomeButton(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, CircleShape),
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(4.dp, Color(0xFF4CAF50))
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color(0xFF4CAF50), Color(0xFF98FF98))))
                .padding(vertical = 20.dp, horizontal = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}
