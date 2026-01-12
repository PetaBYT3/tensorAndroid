package com.xliiicxiv.tensor.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.xliiicxiv.tensor.navigation.NavigationCore
import com.xliiicxiv.tensor.ui.theme.TensorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TensorTheme {
                NavigationCore()
            }
        }
    }
}