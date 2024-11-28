package fr.vitesse.rh.data.repository

import fr.vitesse.rh.data.model.Candidate
import kotlinx.coroutines.flow.Flow

interface CandidateRepository {

    fun getCandidate(id: Long): Flow<Candidate?>

    fun getCandidateList(): Flow<List<Candidate>>

    suspend fun insertCandidate(candidate: Candidate)

    suspend fun updateCandidate(candidate: Candidate)

    suspend fun deleteCandidate(candidate: Candidate)
}