package fr.vitesse.rh.data.common

import fr.vitesse.rh.data.model.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RandomUserApi {
    @GET
    suspend fun getRandomUsers(
        @Url url: String,
        @Query("nat") locale: String = "fr",
        @Query("gender") gender: String
    ): RandomUserResponse
}
