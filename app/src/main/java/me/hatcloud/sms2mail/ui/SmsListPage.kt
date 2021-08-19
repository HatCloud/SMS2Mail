package me.hatcloud.sms2mail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.ui.theme.COLOR_G0
import me.hatcloud.sms2mail.ui.theme.COLOR_G1
import me.hatcloud.sms2mail.utils.formatDate

@Composable
fun SmsListPage(mainViewModel: MainViewModel) {

    val isRefreshing: Boolean by mainViewModel.isRefreshing.observeAsState(false)
    val smsList: List<Sms> by mainViewModel.smsList.observeAsState(emptyList())

    SmsListPage(
        isRefreshing = isRefreshing,
        smsList = smsList,
        onRefresh = { mainViewModel.onRefreshSms() })
}

@Composable
fun SmsListPage(isRefreshing: Boolean, smsList: List<Sms>, onRefresh: () -> Unit) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(smsList) { sms ->
                Column {
                    Text(
                        text = sms.person ?: sms.address ?: "Unknown",
                        color = COLOR_G0,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = sms.date.formatDate(),
                        color = COLOR_G1,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    )

                    Text(
                        text = sms.body,
                        color = COLOR_G1,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSmsListPage() {
    SmsListPage(
        isRefreshing = false,
        smsList = listOf(Sms.generateFakeItem(), Sms.generateFakeItem())
    ) {

    }
}