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

    // Mock the dependencies
    @MockK lateinit var mockRepository: CandidateRepository

    // Declare the ViewModel to test
    private lateinit var viewModel: CandidateViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Initialize MockK
        MockKAnnotations.init(this)

        // Start the test dispatcher
        Dispatchers.setMain(UnconfinedTestDispatcher())

        // Mock the getCandidateList() method to return a flow of a list of candidates
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

        // Use MockK's flowOf to return a flow of candidates
        every { mockRepository.getCandidateList() } returns flowOf(mockCandidates)

        // Initialize the ViewModel and pass the mocked repository
        viewModel = CandidateViewModel(mockRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()  // Reset main dispatcher to avoid test interference
    }

    @Test
    fun `test updateCandidateList updates UI state correctly`() = runTest {
        // Given: Mock candidates have been set up
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

        // Mocking the flow of candidates
        every { mockRepository.getCandidateList() } returns flowOf(mockCandidates)

        // Trigger updateCandidateList
        viewModel.updateCandidateList()

        // Collect the UI state and verify the candidate list
        val job = launch {
            viewModel.uiState.collect { state ->
                assert(state.candidateList.size == 2)
                assert(state.candidateList[0].firstName == "John")
                assert(state.candidateList[1].firstName == "Jane")
            }
        }

        // Optionally, you can cancel the job if needed after the assertion
        job.cancel()
    }

    @Test
    fun `test getConvertedCurrencies updates UI state correctly with fixed rate`() = runTest {
        // Given: A salary expectation and the fixed random rate for testing
        val salaryExpectation = "50000"
        val randomRate = 1.1 // Fixed rate in the method
        val fixedRate = 0.85 * randomRate // Applying fixed rate calculation

        // Collect the UI state before calling the method
        val uiStateFlow = mutableListOf<CandidateUiState>()
        val job = launch {
            viewModel.uiState.collect { uiStateFlow.add(it) }
        }

        // When: Calling the getConvertedCurrencies method
        viewModel.getConvertedCurrencies(salaryExpectation)

        // Then: Verify that the UI state was updated with the correct converted GBP salary
        val expectedConvertedGbp = 50000 * fixedRate
        val formattedExpectedConvertedGbp = "%.2f £".format(expectedConvertedGbp)

        // Wait for the state update to happen and verify the updated state
        delay(100) // Small delay to ensure the state has time to update

        // Verify that the last value in the state flow is the expected value
        assertEquals(formattedExpectedConvertedGbp, uiStateFlow.last().convertedGbpSalary)

        // Cancel the collection job
        job.cancel()
    }






    @Test
    fun `test insertCandidate calls repository insert method`() = runTest {
        // Given: A Candidate with all required fields (id is auto-generated)
        val candidate = Candidate(
            id = 0,  // id is auto-generated, so this value is ignored
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

        // Mock the insertCandidate suspending function
        coEvery { mockRepository.insertCandidate(candidate) } just Runs  // just Runs means it does nothing but completes

        // When: Calling insertCandidate method in the ViewModel
        viewModel.insertCandidate(candidate, mockOnBackClick)

        // Then: Verify that the repository's insertCandidate method was called with the correct candidate
        coVerify { mockRepository.insertCandidate(candidate) }
    }


    @Test
    fun `test updateCandidate calls repository update method`() = runTest {
        // Given: A Candidate with updated details
        val candidate = Candidate(
            id = 1,  // Example ID (assuming it's an existing candidate)
            profilePicture = null,
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = "1990-01-01",
            phoneNumber = "123-456-7890",
            emailAddress = "johndoe@example.com",
            salaryExpectations = "50000",
            note = "Updated notes",
            isFavorite = true  // Let's assume the favorite status is toggled
        )
        val mockOnBackClick: () -> Unit = {}

        // Mock the updateCandidate suspending function
        coEvery { mockRepository.updateCandidate(candidate) } just Runs  // just Runs means it does nothing but completes

        // When: Calling updateCandidate method in the ViewModel
        viewModel.updateCandidate(candidate, mockOnBackClick)

        // Then: Verify that the repository's updateCandidate method was called with the correct candidate
        coVerify { mockRepository.updateCandidate(candidate) }
    }


    @Test
    fun `test toggleFavorite updates favorite status`() = runTest {
        // Given: A candidate list with a candidate having isFavorite = false
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

        // Create an initial UI state with the candidate list
        val initialState = CandidateUiState(candidateList = listOf(candidate))

        // Mock MutableStateFlow to simulate state updates
        val mutableStateFlow = MutableStateFlow(initialState)

        // Use reflection to set the state in the ViewModel (you can use Hilt or directly assign for testing purposes)
        val field = viewModel::class.java.getDeclaredField("_uiState")
        field.isAccessible = true
        field.set(viewModel, mutableStateFlow)

        // When: Calling toggleFavorite on the candidate
        viewModel.toggleFavorite(candidate)

        // Then: Verify that the isFavorite status has been toggled to true
        val updatedCandidate = mutableStateFlow.value.candidateList.first { it.id == candidate.id }
        assertTrue(updatedCandidate.isFavorite)
    }



    @Test
    fun `test deleteCandidate calls repository delete method`() = runTest {
        // Given: A Candidate to be deleted
        val candidate = Candidate(
            id = 1,  // Example ID (assuming it's an existing candidate)
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

        // Mock the deleteCandidate suspending function
        coEvery { mockRepository.deleteCandidate(candidate) } just Runs  // just Runs means it does nothing but completes

        // When: Calling deleteCandidate method in the ViewModel
        viewModel.deleteCandidate(candidate, mockOnBackClick)

        // Then: Verify that the repository's deleteCandidate method was called with the correct candidate
        coVerify { mockRepository.deleteCandidate(candidate) }
    }


}
