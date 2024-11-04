package fr.vitesse.rh.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.vitesse.rh.R
import fr.vitesse.rh.ui.theme.VitesseRHTheme
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000) // Todo: Récupérer les candidats
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        // Todo: Vérifier si le state est vide
        DefaultMessage() // Todo: Si vide, le statuer
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DefaultMessage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.default_message))
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VitesseRHTheme {
        HomeScreen()
    }
}