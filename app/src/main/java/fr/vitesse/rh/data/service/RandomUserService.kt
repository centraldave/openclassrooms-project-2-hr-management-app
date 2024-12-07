package fr.vitesse.rh.data.service

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import fr.vitesse.rh.data.common.RandomUserApi
import fr.vitesse.rh.data.common.RetrofitClient
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.model.RandomUserResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class RandomUserService @Inject constructor() {

    private suspend fun fetchRandomUsersApi(url: String): Result<RandomUserResponse> {
        return try {
            val retrofit = RetrofitClient.createRetrofit(url)
            val apiService = retrofit.create(RandomUserApi::class.java)

            val response = apiService.getRandomUsers(url)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchCandidate(): Result<Candidate?> {
            val url = "https://randomuser.me/api/"

            val response = fetchRandomUsersApi(url = url)
            var candidate: Candidate ?= null

            response.onSuccess { userResponse ->
                val user = userResponse.results.first()
                candidate = Candidate(
                    id = 0,
                    dateOfBirth = generateRandomDateOfBirth(18, 60),
                    firstName = user.name.first,
                    lastName = user.name.last,
                    profilePicture = null,
                    phoneNumber = getRandomPhoneNumber(),
                    emailAddress = getRandomEmailAddress(),
                    salaryExpectations = getRandomSalary(30000.00, 120000.00),
                    note = LoremIpsum(30).values.joinToString(" "),
                    isFavorite = Random.nextBoolean()
                )
            }
        return Result.success(candidate)
    }

    private fun generateRandomDateOfBirth(minAge: Int, maxAge: Int): String {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val minYear = currentYear - maxAge
        val maxYear = currentYear - minAge

        val randomYear = Random.nextInt(minYear, maxYear + 1)
        val randomMonth = Random.nextInt(0, 12)
        val randomDay = Random.nextInt(1, 29)

        val calendar = Calendar.getInstance().apply {
            set(randomYear, randomMonth, randomDay)
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getRandomPhoneNumber(): String {
        val randomDigits = (1..8)
            .map { (0..9).random() }
            .joinToString("")
        return "06$randomDigits"
    }


    private fun getRandomEmailAddress(): String {
        val domains = listOf("gmail.com", "yahoo.com", "outlook.com", "example.com")
        val randomUsernameLength = (5..10).random()
        val username = (1..randomUsernameLength)
            .map { ('a'..'z').random() }
            .joinToString("")
        val domain = domains.random()

        return "$username@$domain"
    }


    private fun getRandomSalary(min: Double, max: Double): String {
        val randomSalary = Random.nextDouble(min, max)
        return "%.2f".format(randomSalary).replace(",", ".")
    }

}