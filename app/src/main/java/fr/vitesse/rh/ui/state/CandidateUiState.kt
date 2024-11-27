package fr.vitesse.rh.ui.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import fr.vitesse.rh.data.model.Candidate

data class CandidateUiState(
    val isLoading: Boolean = true,
    val selectedTab: MutableState<Int> = mutableIntStateOf(0),
    val candidateList: List<Candidate> = emptyList(),
    val convertedUsdSalary: String? = null,
)