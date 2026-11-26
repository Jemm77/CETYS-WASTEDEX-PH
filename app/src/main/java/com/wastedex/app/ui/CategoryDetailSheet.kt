package com.wastedex.app.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wastedex.app.Categoria
import kotlinx.coroutines.delay
import java.io.File

/** CategoryDetailView.swift: ficha, cuento ecológico y galería de fotos. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailSheet(categoria: Categoria, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var cuento by remember { mutableStateOf("") }
    var cargandoCuento by remember { mutableStateOf(true) }
    var errorCuento by remember { mutableStateOf(false) }
    var gallery by remember { mutableStateOf<List<File>>(emptyList()) }
    var fotoABorrar by remember { mutableStateOf<File?>(null) }
    var recarga by remember { mutableIntStateOf(0) }

    LaunchedEffect(categoria, recarga) {
        cargandoCuento = true
        errorCuento = false
        delay(1200) // TODO: reemplazar por tu LLM (ver nuestraLLM.swift)
        cuento = "Había una vez un amigo del planeta que encontró un objeto de ${categoria.nombre.lowercase()}. " +
            "Aprendió que si lo separaba y lo llevaba al lugar correcto, podía convertirse en algo nuevo " +
            "y ayudar a mantener limpios los ríos, los bosques y el aire. ¡Y tú también puedes ayudar!"
        cargandoCuento = false
        gallery = WasteDexStorage.gallery(context, categoria.nombre)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFFD1F7E3)
    ) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { recarga++ }) { Icon(Icons.Filled.Refresh, "Recargar") }
                Text("Detalle", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onDismiss) { Text("Cerrar") }
            }

            // Header
            Row(
                Modifier.fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(categoria.color.copy(alpha = 0.9f), Color(0xCC4CAF50))),
                        RoundedCornerShape(20.dp)
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(categoria.mascota), null,
                    modifier = Modifier.size(84.dp)
                        .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(16.dp)).padding(6.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Tu ficha ecológica", color = Color.White.copy(alpha = 0.95f), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Image(
                            painterResource(categoria.imagen), null,
                            modifier = Modifier.size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)).padding(6.dp)
                        )
                        Text(categoria.nombre, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Text(
                        "Aprende jugando cómo cuidamos el planeta.",
                        color = Color.White.copy(alpha = 0.92f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Cuento
            CardBox(borderColor = Color(0xFF4CAF50)) {
                Text("Cuento ecológico", color = Color(0xFF2E7D32), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                when {
                    cargandoCuento -> Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                        Text("Nuestro amigo está preparando tu cuento...", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                    }
                    errorCuento -> Text("No pudimos crear un cuento ahorita. Intenta recargar.", color = Color.Red, fontWeight = FontWeight.SemiBold)
                    else -> Text(cuento, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Galería
            CardBox(borderColor = Color(0xFF2196F3)) {
                Text("Tu galería", color = Color(0xFF1565C0), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                if (gallery.isEmpty()) {
                    Text("Aún no hay fotos en esta categoría.", color = Color.Gray, fontWeight = FontWeight.SemiBold)
                } else {
                    // Altura fija porque está dentro de un scroll vertical
                    val filas = (gallery.size + 2) / 3
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxWidth().height((filas * 102).dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        userScrollEnabled = false
                    ) {
                        items(gallery, key = { it.absolutePath }) { file ->
                            Box(Modifier.height(92.dp).clip(RoundedCornerShape(12.dp))) {
                                val bmp = remember(file) { BitmapFactory.decodeFile(file.path) }
                                if (bmp != null) {
                                    Image(bmp.asImageBitmap(), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                } else {
                                    Box(Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.2f)))
                                }
                                IconButton(
                                    onClick = { fotoABorrar = file },
                                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(30.dp)
                                        .background(Color.Red.copy(alpha = 0.9f), CircleShape)
                                ) { Icon(Icons.Filled.Delete, "Borrar", tint = Color.White, modifier = Modifier.size(16.dp)) }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    fotoABorrar?.let { target ->
        AlertDialog(
            onDismissRequest = { fotoABorrar = null },
            title = { Text("¿Quieres borrar esta foto?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    target.delete()
                    gallery = WasteDexStorage.gallery(context, categoria.nombre)
                    fotoABorrar = null
                }) { Text("Borrar", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { fotoABorrar = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun CardBox(borderColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth()
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(18.dp))
            .border(2.dp, borderColor.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}
