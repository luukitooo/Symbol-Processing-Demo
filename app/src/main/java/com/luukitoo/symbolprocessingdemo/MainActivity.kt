package com.luukitoo.symbolprocessingdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luukitoo.symbolprocessingdemo.ui.theme.SymbolProcessingDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SymbolProcessingDemoTheme {
                val loginViewModel = viewModel<LoginViewModel>()
                LoginScreen(
                    viewState = loginViewModel.viewState,
                    onEvent = loginViewModel::onEvent
                )
            }
        }
    }
}
