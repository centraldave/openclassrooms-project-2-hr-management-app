package fr.vitesse.rh.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.vitesse.rh.data.model.Candidate
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCandidate(candidate: Candidate)

    @Update
    fun updateCandidate(candidate: Candidate)

    @Delete
    fun deleteCandidate(candidate: Candidate)

    @Query("SELECT * FROM candidate_list WHERE id = :id  LIMIT 1")
    fun getCandidate(id: Long): Flow<Candidate>

    @Query("SELECT * FROM candidate_list ORDER BY lastName ASC")
    fun getCandidateList(): Flow<List<Candidate>>
}
