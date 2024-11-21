package fr.vitesse.rh.data.common

import fr.vitesse.rh.data.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("v4/latest/{baseCurrency}")
    suspend fun getExchangeRates(
        @Path("baseCurrency") baseCurrency: String
    ): CurrencyResponse
}