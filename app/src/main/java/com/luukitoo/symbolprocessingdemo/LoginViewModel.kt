package com.luukitoo.symbolprocessingdemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var viewState by mutableStateOf(LoginViewState())
        private set

    private var loginJob: Job? = null

    // here you can handle generated events.
    fun onEvent(event: LoginViewStateEvents) {
        when (event) {
            is LoginViewStateEvents.UpdateEmail -> updateEmail(event.newValue)
            is LoginViewStateEvents.UpdatePassword -> updatePassword(event.newValue)
            is LoginViewStateEvents.UpdateRepeatPassword -> updateRepeatPassword(event.newValue)
            is LoginViewStateEvents.UpdateRememberUser -> updateRememberUser(event.newValue)
            is LoginViewStateEvents.Login -> login()
            is LoginViewStateEvents.MyCustomEvent -> doSomeStuff(event.myParameter)
        }
    }

    private fun updateEmail(newValue: String) {
        viewState = viewState.copy(
            email = newValue
        )
    }

    private fun updatePassword(newValue: String) {
        viewState = viewState.copy(
            password = newValue
        )
    }

    private fun updateRepeatPassword(newValue: String) {
        viewState = viewState.copy(
            repeatPassword = newValue
        )
    }

    private fun updateRememberUser(newValue: Boolean) {
        viewState = viewState.copy(
            rememberUser = newValue
        )
    }

    private fun login() {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            delay(1000)
            viewState = viewState.copy(
                loggedIn = true
            )
            delay(50)
            viewState = viewState.copy(
                loggedIn = false
            )
        }
    }

    private fun doSomeStuff(myParameter: LoginViewState.MyCustomEventParams) {
        TODO("do whatever you want :P")
    }
}