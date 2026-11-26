package com.wastedex.app

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf

/** Equivalente a CategoriesStore (UserDefaults -> SharedPreferences). */
class CategoriesStore(context: Context) {
    private val prefs = context.getSharedPreferences("wastedex", Context.MODE_PRIVATE)
    private val key = "unlockedCategories"

    val unlocked: SnapshotStateList<String> = mutableStateListOf<String>().apply {
        addAll(prefs.getStringSet(key, emptySet()) ?: emptySet())
    }

    fun unlock(categoria: String) {
        if (categoria !in unlocked) {
            unlocked.add(categoria)
            prefs.edit().putStringSet(key, unlocked.toSet()).apply()
        }
    }

    fun reset() {
        unlocked.clear()
        prefs.edit().remove(key).apply()
    }

    fun highScore(juego: Int): Int = prefs.getInt("minijuego${juego}_high_score", 0)
}
