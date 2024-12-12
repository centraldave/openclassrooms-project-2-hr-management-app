package fr.vitesse.rh.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.vitesse.rh.R
import fr.vitesse.rh.data.service.CandidateInformationService
import fr.vitesse.rh.ui.common.CandidateSearchbar
import fr.vitesse.rh.ui.common.CandidateTabRow
import fr.vitesse.rh.ui.state.CandidateUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    candidateUiState: CandidateUiState,
    onCreateUpdateClick: () -> Unit = {},
) {
    var searchQuery = remember { mutableStateOf("") }

    val filteredCandidates = when (candidateUiState.tabIndex.value) {
        0 -> candidateUiState.candidateList.filter {
            it.firstName.contains(searchQuery.value, ignoreCase = true) ||
                    it.lastName.contains(searchQuery.value, ignoreCase = true)
        }
        1 -> candidateUiState.candidateList.filter {
            it.isFavorite &&
                    (it.firstName.contains(searchQuery.value, ignoreCase = true) ||
                            it.lastName.contains(searchQuery.value, ignoreCase = true))
        }
        else -> emptyList()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.vitesse_hr_logo),
                contentDescription = "Vitesse HR Logo",
                modifier = Modifier
                    .width(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            CandidateSearchbar(searchQuery)

            CandidateTabRow(
                navHostController,
                candidateUiState,
                filteredCandidates
            )
        }

        FloatingActionButton(
            onClick = onCreateUpdateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Candidate",
                tint = Color.White
            )
        }
    }
}
