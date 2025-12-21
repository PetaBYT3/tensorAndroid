package com.xliiicxiv.tensor.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xliiicxiv.tensor.navigation.NavigationCore
import com.xliiicxiv.tensor.page.PageHomeCore
import com.xliiicxiv.tensor.page.PageProfileCore
import com.xliiicxiv.tensor.page.PageSetupCore
import com.xliiicxiv.tensor.page.PageSignInCore
import com.xliiicxiv.tensor.page.PageSignTemplateCore
import com.xliiicxiv.tensor.page.PageSignUpCore
import com.xliiicxiv.tensor.page.PageSplashCore
import com.xliiicxiv.tensor.template.animatedComposable
import com.xliiicxiv.tensor.ui.theme.TensorTheme
import com.xliiicxiv.tensor.viewmodel.ViewModelMain
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

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