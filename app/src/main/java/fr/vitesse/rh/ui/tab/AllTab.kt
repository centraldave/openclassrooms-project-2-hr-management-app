package fr.vitesse.rh.ui.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.service.CandidateService
import fr.vitesse.rh.ui.common.CandidateCell
import fr.vitesse.rh.ui.state.CandidateUiState

@Composable
fun AllTab(
    candidateService: CandidateService,
    navController: NavController,
    onCandidateClick: (Candidate) -> Unit = {},
    onCreationUpdatelick: () -> Unit = {},
    candidateUiState: CandidateUiState
) {
    val query by remember { mutableStateOf("") }
    val filteredCandidates = candidateUiState.candidateList.filter { candidate ->
        candidate.firstName.contains(query, ignoreCase = true) ||
                candidate.lastName.contains(query, ignoreCase = true)
    }

    Column {
        LazyColumn() {
            items(filteredCandidates) { candidate ->
                CandidateCell(
                    candidateService = candidateService,
                    candidate = candidate,
                    onCandidateClick = onCandidateClick
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Item"
            )
        }
    }
}