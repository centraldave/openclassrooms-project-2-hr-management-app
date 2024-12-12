package fr.vitesse.rh

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.data.service.RandomUserService
import fr.vitesse.rh.ui.viewmodel.CandidateViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

//@HiltAndroidTest
//class CandidateViewModelTest {
//
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
//
//    @Inject
//    lateinit var candidateRepository: CandidateRepository
//
//    @Inject
//    lateinit var randomUserService: RandomUserService
//
//    @Inject
//    lateinit var candidateViewModel: CandidateViewModel
//
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setUp() {
//        hiltRule.inject()
//        Dispatchers.setMain(testDispatcher)
//    }
////
////    @Test
////    fun `deleteCandidate should remove candidate from the list`() = runTest {
////        // Arrange
////        val candidateToDelete = Candidate(
////            id = 0,
////            profilePicture = null,
////            firstName = "Amelie",
////            lastName = "Poulin",
////            dateOfBirth = "1990-01-01",
////            phoneNumber = "0600000000",
////            emailAddress = "amelie@poulin.com",
////            salaryExpectations = "50000.00",
////            note = "Son fabuleux destin",
////            isFavorite = false
////        )
////
////        val mockRepository = mockk<CandidateRepository>(relaxed = true)
////
////        coEvery { mockRepository.deleteCandidate(candidateToDelete) } just Runs
////        coEvery { mockRepository.getCandidateList() } returns flowOf(listOf(candidateToDelete))
////
////        candidateViewModel = CandidateViewModel(mockRepository, randomUserService)
////
////        val onBackClick = mockk<() -> Unit>(relaxed = true)
////
////        candidateViewModel.deleteCandidate(candidateToDelete, onBackClick)
////        advanceUntilIdle()
////
////        coVerify { mockRepository.deleteCandidate(candidateToDelete) }
////
////        verify { onBackClick.invoke() }
////
////        candidateViewModel.uiState.collect {
////            assertTrue(it.candidateList.isEmpty())
////        }
////    }
//
//
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//}
//
