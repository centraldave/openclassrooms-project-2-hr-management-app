package fr.vitesse.rh

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.vitesse.rh.data.common.CandidateDatabase
import fr.vitesse.rh.data.dao.CandidateDao
import fr.vitesse.rh.data.model.Candidate
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class CandidateDaoTest {

    private lateinit var database: CandidateDatabase
    private lateinit var candidateDao: CandidateDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CandidateDatabase::class.java
        ).allowMainThreadQueries().build()

        candidateDao = database.candidateDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    suspend fun insertAndRetrieveCandidate() {
        // given
        val candidate = Candidate(
            id = 0,
            dateOfBirth = Date(),
            gender = GenderEnum.FEMALE,
            firstName = "Amélie",
            lastName = "Poulin",
            profilePicture = null,
            phoneNumber = "0123456789",
            salaryExpectations = 30000.0,
            note = "Test",
            isFavorite = false
        )

        // when
        candidateDao.insertCandidate(candidate)
        val insertedCandidateId = candidateDao.getCandidateList().first().first().id

        // then
        val retrievedCandidate = candidateDao.getCandidate(insertedCandidateId).first()
        assertEquals(candidate.firstName, retrievedCandidate?.firstName)
        assertEquals(candidate.lastName, retrievedCandidate?.lastName)
        assertEquals(candidate.phoneNumber, retrievedCandidate?.phoneNumber)
        retrievedCandidate?.salaryExpectations?.let {
            assertEquals(candidate.salaryExpectations,
                it, 0.0)
        }
        assertEquals(candidate.note, retrievedCandidate?.note)
        assertEquals(candidate.isFavorite, retrievedCandidate?.isFavorite)
    }


    @Test
    suspend fun updateCandidate() {
        // given
        val candidate = Candidate(
            id = 0,
            dateOfBirth = Date(),
            gender = GenderEnum.FEMALE,
            firstName = "Amélie",
            lastName = "Poulin",
            profilePicture = null,
            phoneNumber = "0123456789",
            salaryExpectations = 30000.0,
            note = "Test",
            isFavorite = false
        )
        val updatedNote = "Updated candidate"

        // when
        candidateDao.insertCandidate(candidate)
        val insertedCandidateId = candidateDao.getCandidateList().first().first().id
        candidateDao.updateCandidate(candidate.copy(id = insertedCandidateId, note = updatedNote))

        // then
        val updatedCandidate = candidateDao.getCandidate(insertedCandidateId).first()
        assertEquals(updatedNote, updatedCandidate?.note)
    }

    @Test
    suspend fun deleteCandidate()  {
        // given
        val candidate = Candidate(
            id = 0,
            dateOfBirth = Date(),
            gender = GenderEnum.MALE,
            firstName = "John",
            lastName = "Smith",
            profilePicture = null,
            phoneNumber = "1122334455",
            salaryExpectations = 40000.0,
            note = "To be deleted",
            isFavorite = true
        )
        candidateDao.insertCandidate(candidate)
        // when
        candidateDao.deleteCandidate(candidate)
        // then
        val retrievedCandidates = candidateDao.getCandidateList().first()
        assertTrue(retrievedCandidates.isEmpty())
    }
}
