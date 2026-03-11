package com.app_will.geedrapplication.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.ScreenNavigation
import com.app_will.geedrapplication.ui.components.ButtonComponent
import com.app_will.geedrapplication.ui.components.ProgressBar
import com.app_will.geedrapplication.ui.components.TextField
import com.app_will.geedrapplication.ui.components.TitleApp
import com.app_will.geedrapplication.utils.UiEvent

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {

    val context = LocalContext.current

    val isProgressBarActive by loginViewModel.isProgressBarActiveStateFlow.collectAsState()
    val isButtonEnabled by loginViewModel.isEnabledButtonStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.responseUserStateFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                else -> Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        loginViewModel.navigateToMainSharedFlow.collect {
            navController.navigate(it.route) {
                popUpTo(ScreenNavigation.Login.route) { inclusive = true }
            }
        }
    }

    LoginContent(
        isProgressBarActive = isProgressBarActive,
        isButtonEnabled = isButtonEnabled,
        onClickRegister = { login, password ->
            loginViewModel.login(
                userIdentifier = login,
                userPassword = password
            )
        }
    )
}

@Composable
fun LoginContent(
    isProgressBarActive: Boolean,
    isButtonEnabled: Boolean,
    onClickRegister: (String, String) -> Unit
) {
    var login by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        TitleApp(
            text = stringResource(id = R.string.login_screen_title),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .height(200.dp)
                    .align(Alignment.Center)
            ) {

                TextField(
                    maxChar = 30,
                    value = login,
                    onValueChange = { login = it },
                    placeholder = stringResource(id = R.string.login_screen_login_label),
                    maxLines = 1
                )
                TextField(
                    maxChar = 30,
                    value = password,
                    onValueChange = { password = it },
                    placeholder = stringResource(id = R.string.login_screen_password_label),
                    visualTransformation = PasswordVisualTransformation(),
                    maxLines = 1
                )
            }
            ProgressBar(
                isProgressBarActive = isProgressBarActive,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(150.dp)
        ) {
            ButtonComponent(
                onClickNavigate = { onClickRegister(login, password) },
                isEnabled = isButtonEnabled,
                label = stringResource(id = R.string.login_screen_button_label)
            )
        }


    }
}