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
    fun getProfilePicture(candidate: Candidate): Int {
        return candidate.profilePicture ?: getDefaultProfilePicture(candidate.gender)
    }

    fun getDefaultProfilePicture(gender: GenderEnum): Int {
        return R.drawable.default_candidate
    }

    fun getFormattedBirthday(candidate: Candidate, context: Context): String {
        val birthDate = candidate.dateOfBirth

        birthDate?.let {
            val localDate = it.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formattedBirthDate = localDate.format(formatter)

            val age = Period.between(localDate, LocalDate.now()).years

            val suffix = context.getString(R.string.birthdate_sufix)
            return "$formattedBirthDate ($age $suffix)"
        }

        return "Date not available"
    }

    fun getSalaryExpectation(candidate: Candidate): String {
        return "${candidate.salaryExpectations} €"
    }
}