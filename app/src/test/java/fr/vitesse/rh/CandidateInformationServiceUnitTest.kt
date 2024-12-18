package fr.vitesse.rh

import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.service.CandidateInformationService
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class CandidateInformationServiceUnitTest {
    private lateinit var candidate: Candidate
    private lateinit var candidateInformationService: CandidateInformationService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        candidateInformationService = CandidateInformationService()

        candidate = Candidate(
            id = 1,
            profilePicture = null,
            firstName = "Amelie",
            lastName = "Poulin",
            dateOfBirth = "01/01/1990",
            phoneNumber = "0600000000",
            emailAddress = "amelie@poulin.com",
            salaryExpectations = "50000.00",
            note = "Son fabuleux destin",
            isFavorite = false
        )
    }

    @Test
    fun `test getSalaryExpectation returns correct salary expectation`() {
        // given
        // when
        val result = candidateInformationService.getSalaryExpectation(candidate)
        // then
        assertEquals("50000.00 €", result)
    }

    @Test
    fun `test getSalaryExpectation returns formatted salary expectation`() {
        // given
        val candidateWithSalary = candidate.copy(salaryExpectations = "75000.00")
        // when
        val result = candidateInformationService.getSalaryExpectation(candidateWithSalary)
        // then
        assertEquals("75000.00 €", result)
    }

}
