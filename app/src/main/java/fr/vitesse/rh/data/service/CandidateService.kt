package fr.vitesse.rh.data.service

import androidx.compose.ui.res.stringResource
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.model.GenderEnum
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import android.content.Context

class CandidateService {
    private val defaultMaleProfilePicture: Int = R.drawable.profile_picture_default_male_512x512
    private val defaultFemaleProfilePicture: Int = R.drawable.profile_picture_default_female_512x512

    fun getProfilePicture(candidate: Candidate): Int {
        return candidate.profilePicture ?: getDefaultProfilePicture(candidate.gender)
    }

    fun getDefaultProfilePicture(gender: GenderEnum): Int {
        return when (gender) {
            GenderEnum.MALE -> defaultMaleProfilePicture
            GenderEnum.FEMALE -> defaultFemaleProfilePicture
        }
    }

    fun getFormattedBirthday(candidate: Candidate, context: Context): String {
        val birthDate = candidate.dateOfBirth // Assume `birthDate` is nullable (Date?)

        // If birthDate is null, return an empty string or some fallback value
        birthDate?.let {
            // Convert Date to LocalDate
            val localDate = it.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // Format the birth date (e.g., 03/03/1989)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formattedBirthDate = localDate.format(formatter)

            // Calculate the age (e.g., 34 years old)
            val age = Period.between(localDate, LocalDate.now()).years

            // Return the formatted string
            val suffix = context.getString(R.string.birthdate_sufix)
            return "$formattedBirthDate ($age $suffix)"
        }

        // If birthDate is null, return a default value
        return "Date not available"
    }

    fun getSalaryExpectation(candidate: Candidate): String {
        return "${candidate.salaryExpectations} €"
    }
}