package com.wastedex.app.ui

import android.content.Context
import android.graphics.Bitmap
import java.io.File

/** WasteDex.swift: guarda las fotos clasificadas como PNG. */
object WasteDexStorage {
    private fun dir(context: Context) = File(context.cacheDir, "WasteDex").apply { mkdirs() }

    fun save(context: Context, bitmap: Bitmap, label: String): File {
        val file = File(dir(context), "WasteDex_${label}_${System.currentTimeMillis() / 1000}.png")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return file
    }

    fun gallery(context: Context, label: String): List<File> =
        dir(context).listFiles()
            ?.filter { it.extension.lowercase() == "png" && it.name.lowercase().startsWith("wastedex_${label.lowercase()}_") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
}
