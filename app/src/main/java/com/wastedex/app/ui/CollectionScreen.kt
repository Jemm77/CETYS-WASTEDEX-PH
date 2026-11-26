package com.wastedex.app.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wastedex.app.Categoria
import com.wastedex.app.Categorias
import com.wastedex.app.CategoriesStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** ContentView.swift: colección tipo "Pokédex" + botones para tomar/elegir foto. */
@Composable
fun CollectionScreen(store: CategoriesStore, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val config = LocalConfiguration.current
    val isVertical = config.screenHeightDp > config.screenWidthDp

    var bannerText by remember { mutableStateOf<String?>(null) }
    var highlighted by remember { mutableStateOf<String?>(null) }
    var recentlyUnlocked by remember { mutableStateOf<String?>(null) }
    var processing by remember { mutableStateOf(false) }
    var imagen by remember { mutableStateOf<Bitmap?>(null) }
    var clasificacion by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }
    var ultimaFueCamara by remember { mutableStateOf(false) }
    var detalle by remember { mutableStateOf<Categoria?>(null) }

    fun mostrarBanner(texto: String) {
        scope.launch {
            bannerText = texto
            delay(1600)
            bannerText = null
        }
    }

    // TODO: reemplazar por tu modelo (TensorFlow Lite) equivalente a clasificadorDeResiduo_1.mlmodel
    fun clasificar(bmp: Bitmap) {
        imagen = bmp
        processing = true
        scope.launch {
            delay(1200)
            clasificacion = Categorias.todas.random().nombre
            processing = false
            mostrarBanner("Clasificación: $clasificacion")
            showConfirm = true
        }
    }

    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        if (bmp != null) { ultimaFueCamara = true; clasificar(bmp) }
    }
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }?.let {
                ultimaFueCamara = false
                clasificar(it)
            }
        }
    }

    val columnas = if (isVertical) (if (config.screenWidthDp > 520) 4 else 3)
    else if (config.screenWidthDp > 900) 6 else if (config.screenWidthDp > 700) 5 else 4

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF00BF63), Color(0xFF00945D)))
        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnas),
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(
                start = if (isVertical) 14.dp else 8.dp, end = if (isVertical) 14.dp else 8.dp,
                top = 8.dp, bottom = 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(Categorias.todas, key = { _, c -> c.nombre }) { idx, cat ->
                val desbloqueada = cat.nombre in store.unlocked
                CategoryTile(
                    categoria = cat,
                    numero = idx + 1,
                    desbloqueada = desbloqueada,
                    highlighted = highlighted == cat.nombre,
                    recentlyUnlocked = recentlyUnlocked == cat.nombre
                ) {
                    if (desbloqueada) detalle = cat else mostrarBanner("Aún no has desbloqueado esta categoría.")
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                ActionButtonsPanel(
                    onCamara = { camara.launch(null) },
                    onGaleria = { galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                )
            }
        }

        // Banner superior
        AnimatedVisibility(
            visible = bannerText != null,
            modifier = Modifier.align(Alignment.TopCenter).statusBarsPadding().padding(top = 12.dp),
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            Row(
                Modifier.shadow(6.dp, CircleShape).background(Color.White.copy(alpha = 0.92f), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.AutoAwesome, null, tint = Color(0xFFFFC107))
                Text(bannerText ?: "", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }

        // Overlay "Clasificando..."
        if (processing) {
            Box(
                Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier.background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(16.dp)).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Clasificando...", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }
    }

    if (showConfirm) {
        ConfirmClassificationSheet(
            image = imagen,
            classification = clasificacion.ifEmpty { null },
            onConfirm = {
                Categorias.porNombre(clasificacion)?.let { match ->
                    val yaEstaba = match.nombre in store.unlocked
                    scope.launch { highlighted = match.nombre; delay(1200); highlighted = null }
                    if (!yaEstaba) {
                        store.unlock(match.nombre)
                        scope.launch { recentlyUnlocked = match.nombre; delay(900); recentlyUnlocked = null }
                    }
                }
                imagen?.let { runCatching { WasteDexStorage.save(context, it, clasificacion) } }
                showConfirm = false
            },
            onRetake = {
                showConfirm = false
                imagen = null
                if (ultimaFueCamara) camara.launch(null)
            }
        )
    }

    detalle?.let { CategoryDetailSheet(it, onDismiss = { detalle = null }) }
}

@Composable
private fun CategoryTile(
    categoria: Categoria,
    numero: Int,
    desbloqueada: Boolean,
    highlighted: Boolean,
    recentlyUnlocked: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val scale = if (recentlyUnlocked) 1.08f else if (highlighted) 1.04f else 1f
    val fondo = if (desbloqueada)
        Brush.linearGradient(listOf(categoria.color.copy(alpha = 0.95f), categoria.color.copy(alpha = 0.72f), Color.Black.copy(alpha = 0.35f)))
    else
        Brush.linearGradient(listOf(Color(0xF26B6B6B), Color(0xEB474747)))
    val grises = ColorMatrix().apply { setToSaturation(if (desbloqueada) 1.1f else 0.15f) }

    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .shadow(6.dp, shape)
            .background(fondo, shape)
            .border(
                if (desbloqueada) 3.dp else 2.dp,
                if (desbloqueada) categoria.color else Color.White.copy(alpha = 0.55f),
                shape
            )
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        // Fila superior: número + estado
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Pill("%03d".format(numero), Color.White.copy(alpha = 0.95f))
            Pill(if (desbloqueada) "¡LISTO!" else "BLOQ", if (desbloqueada) Color(0xFF66FF66) else Color(0xFFFFEB3B), 9.sp)
        }
        Column(
            Modifier.align(Alignment.Center).padding(top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painterResource(categoria.imagen), null,
                modifier = Modifier.weight(1f, fill = false).fillMaxWidth(0.62f).aspectRatio(1f),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.colorMatrix(grises)
            )
            Text(
                categoria.nombre, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center, maxLines = 1,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.22f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Icon(
            if (desbloqueada) Icons.Filled.Verified else Icons.Filled.Lock, null,
            tint = Color.White,
            modifier = Modifier.align(Alignment.BottomEnd).size(20.dp)
                .background(Color.Black.copy(alpha = 0.22f), CircleShape).padding(3.dp)
        )
    }
}

@Composable
private fun Pill(texto: String, color: Color, size: androidx.compose.ui.unit.TextUnit = 10.sp) {
    Text(
        texto, color = color, fontSize = size, fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.background(Color.Black.copy(alpha = 0.34f), CircleShape)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    )
}

@Composable
private fun ActionButtonsPanel(onCamara: () -> Unit, onGaleria: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        Modifier.fillMaxWidth().padding(top = 8.dp).shadow(10.dp, shape)
            .background(Color.White.copy(alpha = 0.25f), shape)
            .border(2.dp, Color.White.copy(alpha = 0.35f), shape)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionCard("Tomar foto", "Usa la cámara", Icons.Filled.CameraAlt, Color(0xFF31C770), onCamara, Modifier.weight(1f))
        ActionCard("Elegir foto", "Desde galería", Icons.Filled.PhotoLibrary, Color(0xFF17A1EB), onGaleria, Modifier.weight(1f))
    }
}

@Composable
private fun ActionCard(
    titulo: String, subtitulo: String, icon: ImageVector, accent: Color,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Surface(
        onClick = onClick, shape = shape, color = Color.Transparent,
        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f)), modifier = modifier
    ) {
        Column(
            Modifier
                .background(Brush.linearGradient(listOf(accent.copy(alpha = 0.95f), accent.copy(alpha = 0.7f), Color.Black.copy(alpha = 0.3f))))
                .heightIn(min = 92.dp).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(36.dp).background(Color.Black.copy(alpha = 0.2f), CircleShape).padding(8.dp))
                Icon(Icons.Filled.AutoAwesome, null, tint = Color(0xFFFFEB3B), modifier = Modifier.size(14.dp))
            }
            Text(titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Text(subtitulo, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}
