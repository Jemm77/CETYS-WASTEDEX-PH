package com.wastedex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import com.wastedex.app.ui.CollectionScreen
import com.wastedex.app.ui.HomeScreen
import com.wastedex.app.ui.MinijuegosScreen
import com.wastedex.app.ui.SplashScreen

private enum class Destino { SPLASH, HOME, COLECCION, MINIJUEGOS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = CategoriesStore(applicationContext)
        setContent {
            MaterialTheme {
                var destino by rememberSaveable { mutableStateOf(Destino.SPLASH) }
                when (destino) {
                    Destino.SPLASH -> SplashScreen(onFinish = { destino = Destino.HOME })
                    Destino.HOME -> HomeScreen(
                        onColeccion = { destino = Destino.COLECCION },
                        onMinijuegos = { destino = Destino.MINIJUEGOS }
                    )
                    Destino.COLECCION -> CollectionScreen(store, onBack = { destino = Destino.HOME })
                    Destino.MINIJUEGOS -> MinijuegosScreen(store, onBack = { destino = Destino.HOME })
                }
            }
        }
    }
}
