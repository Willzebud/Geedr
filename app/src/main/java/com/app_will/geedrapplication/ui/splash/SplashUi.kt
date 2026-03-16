package com.app_will.geedrapplication.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app_will.feedarticlesjetpackcomposeapplication.ui.splash.SplashViewModel
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.RootNavigation
import com.app_will.geedrapplication.ui.components.ProgressBar

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel
) {
    val isProgressBarActive by splashViewModel.isProgressBarActiveStateFlow.collectAsState()

    splashViewModel.navigate()

    LaunchedEffect(Unit) {
        splashViewModel.navigateToLogin.collect {
            navController.navigate(it.route) {
                popUpTo(RootNavigation.Splash.route) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        splashViewModel.navigateToMain.collect {
            navController.navigate(it.route) {
                popUpTo(RootNavigation.Splash.route) {
                    inclusive = true
                }
            }
        }
    }

    SplashContent(isProgressBarActive = isProgressBarActive)

}


@Composable
fun SplashContent(
    isProgressBarActive: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(400.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)


                )
                Text(
                    text = stringResource(id = R.string.splash_screen_slogan),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                )
            }


            ProgressBar(
                isProgressBarActive = isProgressBarActive,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }


    }
}
