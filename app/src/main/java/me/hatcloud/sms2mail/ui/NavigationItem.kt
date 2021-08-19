package me.hatcloud.sms2mail.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.hatcloud.sms2mail.R

sealed class NavigationItem(
    var route: String,
    @DrawableRes var iconRes: Int,
    @StringRes var titleRes: Int
){
    object Toggle: NavigationItem(
        route = "home",
        iconRes = R.drawable.ic_home_black_24dp,
        titleRes = R.string.title_toggle
    )
    object Messages: NavigationItem(
        route = "message",
        iconRes = R.drawable.ic_notifications_black_24dp,
        titleRes = R.string.title_messages
    )
    object Configuration: NavigationItem(
        route = "configuration",
        iconRes = R.drawable.ic_dashboard_black_24dp,
        titleRes = R.string.title_configuration
    )
}
