package fr.vitesse.rh.data.model

import java.util.Date
import java.util.UUID

data class Candidate(
    val id: UUID,
    val dateOfBirth: Date?,
    val gender: GenderEnum,
    val firstName: String,
    val lastName: String,
    val profilePicture: Int?,
    val phoneNumber: String,
    val salary: Double,
    val note: String,
    val isFavorite: Boolean
)