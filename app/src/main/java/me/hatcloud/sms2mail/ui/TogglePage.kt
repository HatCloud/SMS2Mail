package me.hatcloud.sms2mail.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.ui.theme.Sms2MailColors

@Composable
fun TogglePage(mainViewModel: MainViewModel) {
    TogglePageView(
        isServiceRunning = mainViewModel.running.observeAsState(false),
        onToggle = { mainViewModel.onToggle() }
    )
}

@Composable
private fun TogglePageView(isServiceRunning: State<Boolean>, onToggle: () -> Unit) {

    val btnText = stringResource(id = if (isServiceRunning.value) R.string.stop else R.string.start)
    val toggledBackground = Sms2MailColors().primary
    val untoggledBackground = Sms2MailColors().background
    val backgroundColor by animateColorAsState(
        targetValue = if (isServiceRunning.value)
            toggledBackground
        else
            untoggledBackground,
        animationSpec = tween(durationMillis = 250)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
    ) {
        Button(
            onClick = onToggle
        ) {
            Text(text = btnText)
        }
    }
}


@Preview
@Composable
fun PreviewTogglePage() {
    var isRunning = remember {
        mutableStateOf(false)
    }
    TogglePageView(isRunning) {}
}