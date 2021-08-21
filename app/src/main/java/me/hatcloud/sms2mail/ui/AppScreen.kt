package me.hatcloud.sms2mail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.ui.theme.Sms2MailTheme

@Composable
fun AppScreen(mainViewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    Sms2MailTheme {
        Scaffold(
            topBar = {
                TopBar()
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) {
            Navigation(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController, startDestination = NavigationItem.Toggle.route) {
        composable(NavigationItem.Toggle.route) {
            TogglePage(mainViewModel)
        }
        composable(NavigationItem.Messages.route) {
            SmsListPage(mainViewModel)
        }
        composable(NavigationItem.Configuration.route) {
            ConfigurationPage()
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(title = {
        Text(text = stringResource(id = R.string.app_name))
    })
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Toggle,
        NavigationItem.Messages,
        NavigationItem.Configuration
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                label = { Text(text = stringResource(id = item.titleRes)) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    AppScreen()
}