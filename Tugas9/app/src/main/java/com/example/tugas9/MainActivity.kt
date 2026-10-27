package com.example.tugas9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.tugas9.data.AppDatabase
import com.example.tugas9.ui.LoginScreen
import com.example.tugas9.ui.theme.Tugas9Theme

class MainActivity : ComponentActivity() {
    
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(database.userDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas9Theme {
                LoginScreen(viewModel = loginViewModel)
            }
        }
    }
}
