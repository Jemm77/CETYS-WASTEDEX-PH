package com.wastedex.app.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** ConfirmClassificationView.swift (se muestra como bottom sheet al ~90%). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmClassificationSheet(
    image: Bitmap?,
    classification: String?,
    onConfirm: () -> Unit,
    onRetake: () -> Unit
) {
    // No se puede cerrar deslizando (interactiveDismissDisabled en iOS)
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )
    ModalBottomSheet(onDismissRequest = {}, sheetState = state) {
        Column(
            Modifier.fillMaxWidth().padding(20.dp).navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "¿La clasificación es correcta?",
                fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            )
            image?.let {
                Image(
                    bitmap = it.asImageBitmap(), contentDescription = null,
                    modifier = Modifier.heightIn(max = 260.dp).clip(RoundedCornerShape(12.dp))
                )
            }
            Text(
                if (classification != null) "Detectado: $classification" else "No se pudo determinar la categoría",
                fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    onClick = onRetake, shape = CircleShape,
                    color = Color.Gray.copy(alpha = 0.15f), modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "¡Reintentar!", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                }
                Surface(
                    onClick = onConfirm, shape = CircleShape,
                    color = Color.Transparent, modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "¡Aceptar!", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(Brush.linearGradient(listOf(Color(0xFF4CAF50), Color(0xFF98FF98))))
                            .padding(vertical = 14.dp)
                    )
                }
            }
        }
    }
}
