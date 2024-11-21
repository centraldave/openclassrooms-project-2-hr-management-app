package fr.vitesse.rh.data.service

import fr.vitesse.rh.data.common.CurrencyApi
import fr.vitesse.rh.data.common.RetrofitClient
import fr.vitesse.rh.data.model.CurrencyResponse
import javax.inject.Inject

class CurrencyService @Inject constructor() {

    private suspend fun fetchExchangeRates(baseUrl: String, baseCurrency: String): Result<CurrencyResponse> {
        return try {
            val retrofit = RetrofitClient.createRetrofit(baseUrl)
            val apiService = retrofit.create(CurrencyApi::class.java)
            val response = apiService.getExchangeRates(baseCurrency)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): Result<Double> {
        val url = "https://api.exchangerate-api.com/"
        val response = fetchExchangeRates(url, fromCurrency)

        return response.fold(
            onSuccess = { exchangeRates ->
                val rate = exchangeRates.rates[toCurrency]
                if (rate != null) {
                    val convertedAmount = amount * rate
                    Result.success(convertedAmount)
                } else {
                    Result.failure(Exception("Currency not found"))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}