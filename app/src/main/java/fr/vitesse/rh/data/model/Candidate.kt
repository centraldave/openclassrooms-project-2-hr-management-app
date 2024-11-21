package fr.vitesse.rh.data.model

import androidx.room.Entity
import java.util.Date
import java.util.UUID

@Entity(tableName = "candidates")
data class Candidate(
    val id: UUID,
    val dateOfBirth: Date?,
    val gender: GenderEnum,
    val firstName: String,
    val lastName: String,
    val profilePicture: Int?,
    val phoneNumber: String,
    val salaryExpectations: Double,
    val note: String,
    val isFavorite: Boolean
)