package com.example.investwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.investwallet.home.Home
import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.search.SearchScreen
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val systemUiController = rememberSystemUiController()
            val darkTheme: Boolean = isSystemInDarkTheme()

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = if (darkTheme) Color(0xFF121212) else Color.Transparent,
                    darkIcons = !darkTheme
                )
            }

            InvestWalletTheme(darkTheme) {
                Surface(modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()) {
                    SearchScreen()
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InvestWalletTheme {
        Greeting("Android")
    }
}