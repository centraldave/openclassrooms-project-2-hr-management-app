package fr.vitesse.rh.ui.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.service.CandidateService
import fr.vitesse.rh.ui.common.CandidateCell
import fr.vitesse.rh.ui.state.CandidateUiState

@Composable
fun FavoriteTab(
    candidateService: CandidateService,
    navController: NavController,
    onCandidateClick: (Candidate) -> Unit = {},
    onCreationUpdatelick: () -> Unit = {},
    candidateUiState: CandidateUiState
) {
    val query by remember { mutableStateOf("") }
    val favoriteCandidates = candidateUiState.candidateList.filter { it.isFavorite }

    val filteredFavoriteCandidates = favoriteCandidates.filter { candidate ->
        candidate.firstName.contains(query, ignoreCase = true) ||
                candidate.lastName.contains(query, ignoreCase = true)
    }

    if (filteredFavoriteCandidates.isEmpty()) {
        // Center the "No favorites" message
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.no_favorite),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    } else {
        // Display filtered favorite candidates
        LazyColumn {
            items(filteredFavoriteCandidates) { candidate ->
                CandidateCell(
                    candidateService = candidateService,
                    candidate = candidate,
                    onCandidateClick = onCandidateClick
                )
            }
        }
    }
}