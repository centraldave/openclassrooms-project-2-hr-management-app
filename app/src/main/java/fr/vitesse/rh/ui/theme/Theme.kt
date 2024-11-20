package fr.vitesse.rh.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SoftCoral,
    onPrimary = White,
    onPrimaryContainer = MistyBlue,
    secondary = GoldenSand,
    onSecondary = White,
    onSecondaryContainer = SunflowerGold,
    onTertiary = Midnight
)

@Composable
fun VitesseRHTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}