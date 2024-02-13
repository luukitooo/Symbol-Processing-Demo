package com.luukitoo.symbolprocessingdemo

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewState: LoginViewState,
    // from here you can send generated events.
    onEvent: (LoginViewStateEvents) -> Unit,
) {

    val context = LocalContext.current

    if (viewState.loggedIn) {
        Toast.makeText(context, "Logged in!", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewState.email,
            label = { Text(text = "Email") },
            onValueChange = {
                onEvent(LoginViewStateEvents.UpdateEmail(it))
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewState.password,
            label = { Text(text = "Password") },
            onValueChange = {
                onEvent(LoginViewStateEvents.UpdatePassword(it))
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewState.repeatPassword,
            label = { Text(text = "Repeat Password") },
            onValueChange = {
                onEvent(LoginViewStateEvents.UpdateRepeatPassword(it))
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = viewState.rememberUser,
                onCheckedChange = {
                    onEvent(LoginViewStateEvents.UpdateRememberUser(it))
                }
            )
            Text(text = "Remember me")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(LoginViewStateEvents.Login) }
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Continue",
                fontSize = 18.sp
            )
        }
    }
}