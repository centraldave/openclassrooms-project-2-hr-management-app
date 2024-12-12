package fr.vitesse.rh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.data.service.RandomUserService
import fr.vitesse.rh.ui.state.CandidateUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository,
//    private val randomUserService: RandomUserService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidateUiState())
    val uiState: StateFlow<CandidateUiState> = _uiState.asStateFlow()

    init {
//        viewModelScope.launch {
//            fetchCandidatesOnInit(14)
//        }
        updateCandidateList()
    }

//    suspend fun insertRandomCandidateList(candidateListCount: Int) {
//        withContext(Dispatchers.IO) {
//            repeat(candidateListCount) {
//                val candidate: Candidate? = randomUserService.fetchCandidate().getOrNull()
//                if (candidate != null) {
//                    candidateRepository.insertCandidate(candidate)
//                }
//            }
//        }
//        updateCandidateList()
//        _uiState.update { it.copy(isLoading = false) }
//    }
//
//    private suspend fun fetchCandidatesOnInit(candidateListCount: Int) {
//        _uiState.update { it.copy(isLoading = true) }
//        val candidates = candidateRepository.getCandidateList().firstOrNull()
//        if (candidates.isNullOrEmpty()) {
//            insertRandomCandidateList(candidateListCount)
//        }
//        val candidatesFlow = candidateRepository.getCandidateList()
//        candidatesFlow.collect { candidateList ->
//            _uiState.update { it.copy(candidateList = candidateList, isLoading = false) }
//        }
//    }

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

        if (salary != null) {
            if (_uiState.value.convertedGbpSalary.isNullOrEmpty()) {

                viewModelScope.launch {
                    val randomRateGbp = generateRandomRate(0.85, 1.10)

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


    private fun generateRandomRate(min: Double, max: Double): Double {
        return Random.nextDouble (min, max)
    }

    fun updateCandidateList() {
        viewModelScope.launch {
            val candidatesFlow = candidateRepository.getCandidateList()
            candidatesFlow.collect { candidateList ->
                _uiState.update { it.copy(candidateList = candidateList, isLoading = false) }
            }
        }
    }
}