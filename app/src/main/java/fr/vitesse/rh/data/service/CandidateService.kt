package fr.vitesse.rh.data.service

import fr.vitesse.rh.R
import fr.vitesse.rh.data.model.Candidate
import fr.vitesse.rh.data.model.GenderEnum

class CandidateService {
    private val defaultMaleProfilePicture: Int = R.drawable.profile_picture_default_male_512x512
    private val defaultFemaleProfilePicture: Int = R.drawable.profile_picture_default_female_512x512

    fun getProfilePicture(candidate: Candidate): Int {
        return candidate.profilePicture ?: getDefaultProfilePicture(candidate.gender)
    }

    fun getDefaultProfilePicture(gender: GenderEnum): Int {
        return when (gender) {
            GenderEnum.MALE -> defaultMaleProfilePicture
            GenderEnum.FEMALE -> defaultFemaleProfilePicture
        }
    }
}