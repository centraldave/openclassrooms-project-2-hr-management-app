package fr.vitesse.rh

import dagger.hilt.android.testing.HiltAndroidTest
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.ui.state.CandidateUiState
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class CandidateViewModelTest {

    @MockK lateinit var mockRepository: CandidateRepository

    private lateinit var viewModel: CandidateViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(UnconfinedTestDispatcher())

        val mockCandidates = listOf(
            Candidate(
                id = 1,
                profilePicture = null,
                firstName = "John",
                lastName = "Doe",
                dateOfBirth = "1990-01-01",
                phoneNumber = "123-456-7890",
                emailAddress = "johndoe@example.com",
                salaryExpectations = "50000",
                note = "No notes",
                isFavorite = false
            ),
            Candidate(
                id = 2,
                profilePicture = null,
                firstName = "Jane",
                lastName = "Smith",
                dateOfBirth = "1985-05-12",
                phoneNumber = "987-654-3210",
                emailAddress = "janesmith@example.com",
                salaryExpectations = "60000",
                note = "Good candidate",
                isFavorite = true
            )
        )

        every { mockRepository.getCandidateList() } returns flowOf(mockCandidates)

        viewModel = CandidateViewModel(mockRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getCandidateList updates UI state correctly`() = runTest {
        val mockCandidates = listOf(
            Candidate(
                id = 1,
                profilePicture = null,
                firstName = "John",
                lastName = "Doe",
                dateOfBirth = "1990-01-01",
                phoneNumber = "123-456-7890",
                emailAddress = "johndoe@example.com",
                salaryExpectations = "50000",
                note = "No notes",
                isFavorite = false
            ),
            Candidate(
                id = 2,
                profilePicture = null,
                firstName = "Jane",
                lastName = "Smith",
                dateOfBirth = "1985-05-12",
                phoneNumber = "987-654-3210",
                emailAddress = "janesmith@example.com",
                salaryExpectations = "60000",
                note = "Good candidate",
                isFavorite = true
            )
        )

        every { mockRepository.getCandidateList() } returns flowOf(mockCandidates)

        viewModel.getCandidateList()

        val job = launch {
            viewModel.uiState.collect { state ->
                assert(state.candidateList.size == 2)
                assert(state.candidateList[0].firstName == "John")
                assert(state.candidateList[1].firstName == "Jane")
            }
        }

        job.cancel()
    }

    @Test
    fun `test getConvertedCurrencies updates UI state correctly with fixed rate`() = runTest {
        val salaryExpectation = "50000"
        val randomRate = 1.1
        val fixedRate = 0.85 * randomRate

        val uiStateFlow = mutableListOf<CandidateUiState>()
        val job = launch {
            viewModel.uiState.collect { uiStateFlow.add(it) }
        }

        viewModel.getConvertedCurrencies(salaryExpectation)

        val expectedConvertedGbp = 50000 * fixedRate
        val formattedExpectedConvertedGbp = "%.2f £".format(expectedConvertedGbp)

        delay(100)

        assertEquals(formattedExpectedConvertedGbp, uiStateFlow.last().convertedGbpSalary)

        job.cancel()
    }

    @Test
    fun `test insertCandidate calls repository insert method`() = runTest {
        val candidate = Candidate(
            id = 0,
            profilePicture = null,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "1990-01-01",
            phoneNumber = "123-456-7890",
            emailAddress = "johndoe@example.com",
            salaryExpectations = "50000",
            note = "No notes",
            isFavorite = false
        )
        val mockOnBackClick: () -> Unit = {}

        coEvery { mockRepository.insertCandidate(candidate) } just Runs

        viewModel.insertCandidate(candidate, mockOnBackClick)

        coVerify { mockRepository.insertCandidate(candidate) }
    }

    @Test
    fun `test updateCandidate calls repository update method`() = runTest {
        val candidate = Candidate(
            id = 1,
            profilePicture = null,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "1990-01-01",
            phoneNumber = "123-456-7890",
            emailAddress = "johndoe@example.com",
            salaryExpectations = "50000",
            note = "Updated notes",
            isFavorite = true
        )
        val mockOnBackClick: () -> Unit = {}

        coEvery { mockRepository.updateCandidate(candidate) } just Runs

        viewModel.updateCandidate(candidate, mockOnBackClick)

        coVerify { mockRepository.updateCandidate(candidate) }
    }

    @Test
    fun `test deleteCandidate calls repository delete method`() = runTest {
        val candidate = Candidate(
            id = 1,
            profilePicture = null,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "1990-01-01",
            phoneNumber = "123-456-7890",
            emailAddress = "johndoe@example.com",
            salaryExpectations = "50000",
            note = "No notes",
            isFavorite = false
        )
        val mockOnBackClick: () -> Unit = {}

        coEvery { mockRepository.deleteCandidate(candidate) } just Runs

        viewModel.deleteCandidate(candidate, mockOnBackClick)

        coVerify { mockRepository.deleteCandidate(candidate) }
    }

    @Test
    fun `test toggleFavorite updates favorite status`() = runTest {
        val candidate = Candidate(
            id = 1,
            profilePicture = null,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "1990-01-01",
            phoneNumber = "123-456-7890",
            emailAddress = "johndoe@example.com",
            salaryExpectations = "50000",
            note = "No notes",
            isFavorite = false
        )

        val initialState = CandidateUiState(candidateList = listOf(candidate))

        val mutableStateFlow = MutableStateFlow(initialState)

        val field = viewModel::class.java.getDeclaredField("_uiState")
        field.isAccessible = true
        field.set(viewModel, mutableStateFlow)

        viewModel.toggleFavorite(candidate)

        val updatedCandidate = mutableStateFlow.value.candidateList.first { it.id == candidate.id }
        assertTrue(updatedCandidate.isFavorite)
    }
}
