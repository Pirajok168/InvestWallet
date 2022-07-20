package com.example.investwallet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.example.investwallet.shared.Greeting
import com.example.investwallet.shared.core.api.apiModule
import com.example.investwallet.shared.di.EngineSDK
import com.example.investwallet.ui.search.SearchScreen
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val systemUiController = rememberSystemUiController()
            val darkTheme: Boolean = isSystemInDarkTheme()
            
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val a = EngineSDK.apiModule.repo.fetchTickers()
                    Log.e("test_kmm", a)
                } catch (e: Exception) {
                    Log.e("test_kmm", e.message.toString())
                }
            }

           /* SideEffect {
                systemUiController.setSystemBarsColor(
                    color = if (darkTheme) Color(0xFF121212) else Color.Transparent,
                    darkIcons = !darkTheme
                )
            }

            InvestWalletTheme(darkTheme) {
                Surface(modifier = Modifier
                    .fillMaxSize()) {
                    InvestWalletApp()
                }

            }*/
        }
    }
}

