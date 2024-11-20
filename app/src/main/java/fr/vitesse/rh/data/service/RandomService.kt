package fr.vitesse.rh.data.service

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import fr.vitesse.rh.data.common.RetrofitClient
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.model.GenderEnum
import fr.vitesse.rh.data.model.RandomUserResponse
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class RandomService @Inject constructor() {
    private val api = RetrofitClient.apiService

    private suspend fun fetchRandomUsersApi(gender: String): Result<RandomUserResponse> {
        return try {
            val response = api.getRandomUsers(gender = gender)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateCandidates(count: Int): Result<List<Candidate>> {
        val candidates = mutableListOf<Candidate>()

        repeat(count) {
            val gender: GenderEnum = GenderEnum.entries.random()
            val response = fetchRandomUsersApi(gender.name.lowercase())

            response.onSuccess { userResponse ->
                val user = userResponse.results.first()
                candidates.add(
                    Candidate(
                        id = UUID.randomUUID(),
                        dateOfBirth = generateRandomDateOfBirth(18, 60),
                        gender = gender,
                        firstName = user.name.first,
                        lastName = user.name.last,
                        profilePicture = null,
                        phoneNumber = getRandomPhoneNumber(),
                        salary = getRandomSalary(30000.00, 120000.00),
                        note = LoremIpsum(30).values.joinToString(" "),
                        isFavorite = Random.nextBoolean()
                    )
                )
            }
        }
        return Result.success(candidates)
    }

    private fun generateRandomDateOfBirth(minAge: Int, maxAge: Int): Date {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val minYear = currentYear - maxAge
        val maxYear = currentYear - minAge
        val randomYear = Random.nextInt(minYear, maxYear + 1)
        val randomMonth = Random.nextInt(0, 12)
        val randomDay = Random.nextInt(1, 29)
        val calendar = Calendar.getInstance()
        calendar.set(randomYear, randomMonth, randomDay)

        return calendar.time
    }

    private fun getRandomPhoneNumber(): String {
        val randomDigits = (1..9).random()
        return "06$randomDigits"
    }

    private fun getRandomSalary(min: Double, max: Double): Double {

        return Random.nextDouble(min, max)
    }
}