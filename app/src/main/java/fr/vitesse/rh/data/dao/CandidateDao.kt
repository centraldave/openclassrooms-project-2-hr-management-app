package fr.vitesse.rh.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.vitesse.rh.data.model.Candidate
import java.util.UUID

@Dao
interface CandidateDao {

    @Insert
    suspend fun insert(candidate: Candidate)

    @Insert
    suspend fun insertAll(candidates: List<Candidate>)

    @Update
    suspend fun update(candidate: Candidate)

    @Delete
    suspend fun delete(candidate: Candidate)

    @Query("SELECT * FROM candidates")
    suspend fun getAllCandidates(): List<Candidate>

    @Query("SELECT * FROM candidates WHERE id = :id")
    suspend fun getCandidateById(id: UUID): Candidate?
}