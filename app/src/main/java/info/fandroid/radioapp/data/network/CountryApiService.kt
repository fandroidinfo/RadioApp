package info.fandroid.radioapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApiService {
    @GET("countries")
    suspend fun getCountries(
        @Query("hidebroken") hidebroken: Boolean = true
    ): List<CountryResponse>
}