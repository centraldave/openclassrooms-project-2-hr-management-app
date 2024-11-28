package fr.vitesse.rh.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "candidate_list")
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val dateOfBirth: Date,
    val gender: GenderEnum,
    val firstName: String,
    val lastName: String,
    val profilePicture: Int?,
    val phoneNumber: String,
    val salaryExpectations: Double,
    val note: String,
    val isFavorite: Boolean
)