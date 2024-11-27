package fr.vitesse.rh.data.repository

import fr.vitesse.rh.data.model.Candidate
import javax.inject.Inject

class CandidateRepository @Inject constructor(
    // Todo: Dao ROOM
) {
    fun getFavoriteList(): List<Candidate> {
        TODO("Apres Room")
    }
}