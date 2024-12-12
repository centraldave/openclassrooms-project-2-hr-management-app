
import org.junit.Assert.*
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.vitesse.rh.data.common.CandidateDatabase
import fr.vitesse.rh.data.dao.CandidateDao
import fr.vitesse.rh.data.model.Candidate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Context
import androidx.test.core.app.ApplicationProvider

@RunWith(AndroidJUnit4::class)
class CandidateDaoInstrumentTest {

    private lateinit var database: CandidateDatabase
    private lateinit var candidateDao: CandidateDao
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            CandidateDatabase::class.java
        ).allowMainThreadQueries().build()

        candidateDao = database.candidateDao()
    }

    @Test
    fun testInsertCandidate() = runBlocking {
        // given
        val candidate = Candidate(
            id = 0,
            profilePicture = null,
            firstName = "Amelie",
            lastName = "Poulin",
            dateOfBirth = "1990-01-01",
            phoneNumber = "0600000000",
            emailAddress = "amelie@poulin.com",
            salaryExpectations = "50000.00",
            note = "Son fabuleux destin",
            isFavorite = false
        )
        // when
        candidateDao.insertCandidate(candidate)
        // then
        val insertedCandidate = candidateDao.getCandidateList().first().firstOrNull {
            it.firstName == candidate.firstName && it.lastName == candidate.lastName
        }
        assertNotNull(insertedCandidate)
        assertEquals(candidate.firstName, insertedCandidate?.firstName)
        assertEquals(candidate.lastName, insertedCandidate?.lastName)
        assertEquals(candidate.phoneNumber, insertedCandidate?.phoneNumber)
        assertEquals(candidate.emailAddress, insertedCandidate?.emailAddress)
        assertEquals(candidate.salaryExpectations, insertedCandidate?.salaryExpectations)
        assertEquals(candidate.note, insertedCandidate?.note)
        assertEquals(candidate.isFavorite, insertedCandidate?.isFavorite)
    }

    @Test
    fun testUpdateCandidate() = runBlocking {
        // given
        val candidate = Candidate(
            id = 0,
            profilePicture = null,
            firstName = "Amelie",
            lastName = "Poulin",
            dateOfBirth = "1990-01-01",
            phoneNumber = "0600000000",
            emailAddress = "amelie@poulin.com",
            salaryExpectations = "50000.00",
            note = "Son fabuleux destin",
            isFavorite = false
        )
        candidateDao.insertCandidate(candidate)
        // when
        val updatedCandidate = candidateDao.getCandidateList().first().first().copy(lastName = "Durand")
        candidateDao.updateCandidate(updatedCandidate)
        // then
        val retrievedUpdatedCandidate: Candidate = candidateDao.getCandidateList().first().first()
        assertNotNull("Retrieved updated candidate should not be null", retrievedUpdatedCandidate)
        assertEquals("Durand", retrievedUpdatedCandidate?.lastName)
        assertEquals(updatedCandidate.firstName, retrievedUpdatedCandidate?.firstName)
        assertEquals(updatedCandidate.phoneNumber, retrievedUpdatedCandidate?.phoneNumber)
        assertEquals(updatedCandidate.emailAddress, retrievedUpdatedCandidate?.emailAddress)
        assertEquals(updatedCandidate.salaryExpectations, retrievedUpdatedCandidate?.salaryExpectations)
        assertEquals(updatedCandidate.note, retrievedUpdatedCandidate?.note)
        assertEquals(updatedCandidate.isFavorite, retrievedUpdatedCandidate?.isFavorite)
    }

    @Test
    fun testDeleteCandidate() = runBlocking {
        // given
        val candidate = Candidate(
            id = 0,
            profilePicture = null,
            firstName = "Amelie",
            lastName = "Poulin",
            dateOfBirth = "1990-01-01",
            phoneNumber = "0600000000",
            emailAddress = "amelie@poulin.com",
            salaryExpectations = "50000.00",
            note = "Son fabuleux destin",
            isFavorite = false
        )
        candidateDao.insertCandidate(candidate)
        // when
        candidateDao.deleteCandidate(candidate)
        // then
        val retrievedDeletedCandidate = candidateDao.getCandidate(candidate.id).first()
        assertNull(retrievedDeletedCandidate)
    }

    @After
    fun tearDown() {
        database.close()
    }
}
