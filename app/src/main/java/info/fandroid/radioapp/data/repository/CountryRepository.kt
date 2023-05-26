package info.fandroid.radioapp.data.repository

import info.fandroid.radioapp.data.network.CountryApiService
import info.fandroid.radioapp.data.network.CountryResponse
import info.fandroid.radioapp.data.room.Country
import info.fandroid.radioapp.data.room.CountryDao
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class CountryRepository @Inject constructor(
    private val countryApi: CountryApiService,
    private val countryDao: CountryDao
) {

    fun listOfCountries(): Flow<List<Country>> = flow {
        val countriesFromApi = countryApi.getCountries().toCountryList()
        val selectedCountryCodes = countryDao.getAllCountries().first().map { it.countryCode }

        countriesFromApi.forEach { it.isSelected = it.countryCode in selectedCountryCodes }

        emit(countriesFromApi.sortedByDescending { it.isSelected })
    }

    fun getCountOfSelectedCountries(): Flow<Int> {
        return countryDao.getCountOfCountries()
    }

    suspend fun setSelectedCountry(country: Country) {
        countryDao.insertCountry(country)
    }

    suspend fun removeSelectedCountry(country: Country) {
        countryDao.deleteCountry(country)
    }

    private fun List<CountryResponse>.toCountryList(): List<Country> {
        return this.map { response ->
            Country(
                name = response.countryCode?.let { Locale("", it).displayCountry } ?: "",
                countryCode = response.countryCode ?: "",
                stationCount = response.stationCount ?: 0,
                isSelected = false
            )
        }
    }
}