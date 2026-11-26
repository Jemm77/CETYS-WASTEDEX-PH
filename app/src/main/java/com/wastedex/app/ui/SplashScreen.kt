package com.wastedex.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wastedex.app.R
import kotlinx.coroutines.delay

/** PantallaDeCarga.swift */
@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "spin")
    val angle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "angle"
    )

    LaunchedEffect(Unit) {
        delay(2000)
        onFinish()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF00BF63)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.carga),
            contentDescription = null,
            modifier = Modifier.size(300.dp).rotate(angle)
        )
    }
}
