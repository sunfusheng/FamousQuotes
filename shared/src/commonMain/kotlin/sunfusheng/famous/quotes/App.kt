package sunfusheng.famous.quotes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import sunfusheng.famous.quotes.ui.QuoteScreen
import sunfusheng.famous.quotes.ui.SettingsScreen

private val AppColorScheme = lightColorScheme(
    primary = Color(0xFFA2673C),
    onPrimary = Color.White,
    secondary = Color(0xFF7A6652),
    background = Color(0xFFFDF9F3),
    surface = Color(0xFFFFFDF9),
)

@Composable
fun App() {
    MaterialTheme(colorScheme = AppColorScheme) {
        var showSettings by rememberSaveable { mutableStateOf(false) }
        if (showSettings) {
            SettingsScreen(onBack = { showSettings = false })
        } else {
            QuoteScreen(
                category = "名人名言",
                onOpenSettings = { showSettings = true },
            )
        }
    }
}
