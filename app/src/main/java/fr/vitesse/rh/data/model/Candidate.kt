package fr.vitesse.rh.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "candidate_list")
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val profilePicture: Int?,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val phoneNumber: String,
    val emailAddress: String,
    val salaryExpectations: String,
    val note: String,
    val isFavorite: Boolean
)