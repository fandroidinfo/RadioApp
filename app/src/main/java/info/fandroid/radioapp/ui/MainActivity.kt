package info.fandroid.radioapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import dagger.hilt.android.AndroidEntryPoint
import info.fandroid.radioapp.ui.navigation.Destination
import info.fandroid.radioapp.ui.navigation.Navigator
import info.fandroid.radioapp.ui.navigation.ProvideNavHostController
import info.fandroid.radioapp.ui.screen.*
import info.fandroid.radioapp.ui.station.BottomBar
import info.fandroid.radioapp.ui.theme.RadioAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var startDestination = Destination.countries
        if (intent?.action == ACTION_RADIO_NOTIFICATION_CLICK) {
            startDestination = Destination.home
        }
        setContent {
            RadioApp(
                startDestination = startDestination,
                backPressedDispatcher = onBackPressedDispatcher
            )
        }
    }
}

@Composable
fun RadioApp(
    startDestination: String,
    backPressedDispatcher: OnBackPressedDispatcher
) {
    RadioAppTheme {
        ProvideNavHostController {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(Navigator.current, startDestination) {
                    composable(Destination.home) { HomeScreen() }
                    composable(Destination.countries) { CountryScreen() }
                    composable(Destination.favorites) { FavoritesScreen() }

                    composable(Destination.radioScreen) {
                        RadioScreen()
                    }

                    composable(
                        Destination.radioStation,
                        deepLinks = listOf(navDeepLink { uriPattern = "https://at1.api.radio-browser.info/json/url/{id}" })
                    ) { backStackEntry ->
                        RadioDetailScreen(
                            radioId = backStackEntry.arguments?.getString("id"),
                        )
                    }
                }
                BottomBar(modifier = Modifier.align(Alignment.BottomCenter))
                PlayerScreen(backPressedDispatcher)
            }
        }
    }
}