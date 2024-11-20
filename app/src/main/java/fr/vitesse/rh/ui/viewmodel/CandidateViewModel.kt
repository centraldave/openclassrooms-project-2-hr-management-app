package fr.vitesse.rh.ui.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.data.service.RandomService
import fr.vitesse.rh.ui.state.CandidateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository,
    private val randomService: RandomService
): ViewModel(){
    private val _uiState = MutableStateFlow(CandidateUiState())
    val uiState: StateFlow<CandidateUiState> = _uiState.asStateFlow()

    fun readCandidateList() {
        viewModelScope.launch {
            // Todo: Lire candidateRepository
            _uiState.value = randomService.generateCandidates(10).getOrNull()?.let {
                CandidateUiState(false, mutableIntStateOf(0), it)
            }!!
        }
    }
}