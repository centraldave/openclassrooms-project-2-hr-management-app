package fr.vitesse.rh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.ui.state.CandidateUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidateUiState())
    val uiState: StateFlow<CandidateUiState> = _uiState.asStateFlow()

    init {
        getCandidateList()
    }

    fun toggleFavorite(candidate: Candidate) {
        _uiState.update { currentState ->
            val updatedCandidates = currentState.candidateList.map { existingCandidate ->
                if (existingCandidate.id == candidate.id) {
                    existingCandidate.copy(isFavorite = !existingCandidate.isFavorite)
                } else {
                    existingCandidate
                }
            }
            currentState.copy(candidateList = updatedCandidates)
        }
    }

    suspend fun deleteCandidate(candidate: Candidate, onBackClick: () -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.deleteCandidate(candidate)

            withContext(Dispatchers.Main) {
                onBackClick()
            }
        }
    }


    fun insertCandidate(candidate: Candidate, onBackClick: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.insertCandidate(candidate)

            withContext(Dispatchers.Main) {
                onBackClick()
            }
        }
    }

    fun updateCandidate(candidate: Candidate, onBackClick: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.updateCandidate(candidate)

            withContext(Dispatchers.Main) {
                onBackClick()
            }
        }
    }


    fun getConvertedCurrencies(salaryExpectation: String) {
        val salary = salaryExpectation.toDoubleOrNull()
        val randomRate = 1.1

        if (salary != null) {
            if (_uiState.value.convertedGbpSalary.isNullOrEmpty()) {

                viewModelScope.launch {
                    val randomRateGbp = 0.85 *randomRate

                    val convertedGbp = salary * randomRateGbp

                    val formattedGbp = "%.2f £".format(convertedGbp)

                    _uiState.update {
                        it.copy(
                            convertedGbpSalary = formattedGbp
                        )
                    }
                }
            }
        } else {
            println("Invalid salary input")
        }
    }


    fun getCandidateList() {
        viewModelScope.launch {
            val candidatesFlow = candidateRepository.getCandidateList()
            candidatesFlow.collect { candidateList ->
                _uiState.update { it.copy(candidateList = candidateList, isLoading = false) }
            }
        }
    }
}
