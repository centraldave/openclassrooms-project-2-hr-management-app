package fr.vitesse.rh.data.service

import android.content.Context
import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class CandidateInformationService {

    fun getFormattedBirthday(candidate: Candidate, context: Context): String {
        val birthDate = candidate.dateOfBirth

        try {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val localDate = LocalDate.parse(birthDate, dateFormatter)

            val formattedBirthDate = localDate.format(dateFormatter)

            val age = Period.between(localDate, LocalDate.now()).years

            val suffix = context.getString(R.string.birthdate_sufix)

            return "$formattedBirthDate ($age $suffix)"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "Date not available"
    }

    fun getSalaryExpectation(candidate: Candidate): String {
        return "${candidate.salaryExpectations} €"
    }
}