package fr.vitesse.rh.data.service

import fr.vitesse.rh.data.dao.CandidateDao
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.repository.CandidateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CandidateService @Inject constructor(private val candidateDao: CandidateDao): CandidateRepository {
    override fun getCandidate(id: Long): Flow<Candidate?> = candidateDao.getCandidate(id)

    override fun getCandidateList(): Flow<List<Candidate>> = candidateDao.getCandidateList()

    override suspend fun insertCandidate(candidate: Candidate) = candidateDao.insertCandidate(candidate)

    override suspend fun updateCandidate(candidate: Candidate) = candidateDao.updateCandidate(candidate)

    override suspend fun deleteCandidate(candidate: Candidate) = candidateDao.deleteCandidate(candidate)
}