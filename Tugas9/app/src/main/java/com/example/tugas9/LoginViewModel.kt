package com.example.tugas9

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugas9.data.User
import com.example.tugas9.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(username: String, passwordHash: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val user = userDao.getUserByUsername(username)
            if (user != null && user.passwordHash == passwordHash) {
                _loginState.value = LoginState.Success("Login berhasil!")
            } else {
                _loginState.value = LoginState.Error("Username atau password salah")
            }
        }
    }

    fun register(username: String, passwordHash: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                _loginState.value = LoginState.Error("Username sudah terdaftar")
            } else {
                val newUser = User(username = username, passwordHash = passwordHash)
                userDao.insertUser(newUser)
                _loginState.value = LoginState.Success("Registrasi berhasil, silakan login")
            }
        }
    }
    
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
