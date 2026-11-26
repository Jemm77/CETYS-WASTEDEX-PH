package com.wastedex.app

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/** Equivalente a las clases del modelo CoreML + helpers de CategoryDetailView/ContentView. */
data class Categoria(
    val nombre: String,
    @DrawableRes val imagen: Int,
    val color: Color,
    @DrawableRes val mascota: Int
)

object Categorias {
    val todas: List<Categoria> = listOf(
        Categoria("Basura", R.drawable.res_basura, Color(0xFF9E9E9E), R.drawable.mascota_perro),
        Categoria("Baterías", R.drawable.res_baterias, Color(0xFFFBC02D), R.drawable.mascota_elefante),
        Categoria("Biológico", R.drawable.res_biologico, Color(0xFF43A047), R.drawable.mascota_gallina),
        Categoria("Calzado", R.drawable.res_calzado, Color(0xFF8D6E63), R.drawable.mascota_perro),
        Categoria("Cartón", R.drawable.res_carton, Color(0xFF795548), R.drawable.mascota_jirafa),
        Categoria("Electrónicos", R.drawable.res_electronicos, Color(0xFF3F51B5), R.drawable.mascota_jirafa),
        Categoria("Foco", R.drawable.res_foco, Color(0xFFFF9800), R.drawable.mascota_pajaro),
        Categoria("Metal", R.drawable.res_metal, Color(0xFF607D8B), R.drawable.mascota_perro),
        Categoria("Papel", R.drawable.res_papel, Color(0xFF1E88E5), R.drawable.mascota_conejo),
        Categoria("Plástico", R.drawable.res_plastico, Color(0xFFEC407A), R.drawable.mascota_pajaro),
        Categoria("Ropa", R.drawable.res_ropa, Color(0xFF8E24AA), R.drawable.mascota_conejo),
        Categoria("Tóxico", R.drawable.res_toxico, Color(0xFFD32F2F), R.drawable.mascota_gallina),
        Categoria("Vidrio", R.drawable.res_vidrio, Color(0xFF00897B), R.drawable.mascota_elefante)
    )

    fun porNombre(nombre: String): Categoria? =
        todas.firstOrNull { it.nombre.equals(nombre, ignoreCase = true) }
}
