package fr.vitesse.rh.ui.common

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import fr.vitesse.rh.NoDataMessage
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.ui.screen.Screen
import fr.vitesse.rh.ui.state.CandidateUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateTabRow(
    navHostController: NavHostController,
    candidateUiState: CandidateUiState,
    filteredCandidates: List<Candidate>
) {
    val tabNameList = listOf(stringResource(R.string.all_tab), stringResource(R.string.favorite_tab))

    TabRow(
        selectedTabIndex = candidateUiState.tabIndex.value
    ) {
        tabNameList.forEachIndexed { index, title ->
            Tab(
                selected = candidateUiState.tabIndex.value == index,
                onClick = { candidateUiState.tabIndex.value = index },
                text = { Text(text = title) }
            )
        }
    }

    if (filteredCandidates.isNotEmpty()) {
        LazyColumn {
            items(filteredCandidates) { candidate ->
                CandidateCell(
                    candidate = candidate,
                    onCandidateClick = {
                        navHostController.navigate(
                            Screen.DetailCandidate.createRoute(candidateId = candidate.id.toString())
                        )
                    }
                )
            }
        }
    } else {
        NoDataMessage()
    }
}
