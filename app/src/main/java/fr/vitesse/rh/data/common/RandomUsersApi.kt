package fr.vitesse.rh.data.common

import fr.vitesse.rh.data.model.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("api/")
    suspend fun getRandomUsers(
        @Query("nat") locale: String = "fr",
        @Query("gender") gender: String
    ): RandomUserResponse
}