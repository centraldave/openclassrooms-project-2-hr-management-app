package fr.vitesse.rh.ui.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.data.service.CurrencyService
import fr.vitesse.rh.data.service.RandomUserService
import fr.vitesse.rh.ui.state.CandidateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository,
    private val randomUserService: RandomUserService,
    private val currencyService: CurrencyService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidateUiState())
    val uiState: StateFlow<CandidateUiState> = _uiState.asStateFlow()

    fun readCandidateList() {
        viewModelScope.launch {
            _uiState.value = randomUserService.generateCandidates(10).getOrNull()?.let {
                CandidateUiState(false, mutableIntStateOf(0), it)
            }!!
        }
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

    fun editCandidate(candidate: Candidate) {
        // Todo: Naviguer vers l'edit
    }

    fun deleteCandidate(candidate: Candidate, onBackClick: () -> Unit) {
        val updatedCandidates = _uiState.value.candidateList.filterNot {
            it.id == candidate.id
        }
        _uiState.value = _uiState.value.copy(candidateList = updatedCandidates)
        onBackClick() // Navigate away after deletion
    }

    fun getConvertedUsdFromEur(salaryExpectation: Double) {
        if (_uiState.value.convertedUsdSalary.isNullOrEmpty()) {
            viewModelScope.launch {
                val result = currencyService.convertCurrency(
                    amount = salaryExpectation,
                    fromCurrency = "EUR",
                    toCurrency = "USD"
                )

                result.onSuccess { convertedAmount ->
                    val formattedAmount = "%.2f $".format(convertedAmount)
                    _uiState.value = _uiState.value.copy(convertedUsdSalary = formattedAmount)
                }.onFailure {

                    _uiState.value = _uiState.value.copy(convertedUsdSalary = (salaryExpectation * 1.10).toString())
                }
            }
        }
    }

    fun getFavoriteList(): List<Candidate> {
        return candidateRepository.getFavoriteList()
    }
}