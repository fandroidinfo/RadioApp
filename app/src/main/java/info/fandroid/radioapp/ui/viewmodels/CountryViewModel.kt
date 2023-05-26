package info.fandroid.radioapp.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import info.fandroid.radioapp.data.repository.CountryRepository
import info.fandroid.radioapp.data.room.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val countryRepository: CountryRepository
) : ViewModel() {

    var countryUiState: CountryUiState by mutableStateOf(CountryUiState.Loading)
        private set

    var searchWidgetState by mutableStateOf(SearchWidgetState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    val countOfSelectedCountries: StateFlow<Int> = countryRepository
        .getCountOfSelectedCountries()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        searchWidgetState = newValue
    }

    fun updateSearchTextState(newValue: String) {
        searchTextState = newValue
    }

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            countryUiState = CountryUiState.Loading

            countryUiState = try {
                val countries = countryRepository.listOfCountries().first()
                CountryUiState.Success(countries)
            } catch (e: Exception) {
                CountryUiState.Error
            }
        }
    }

    fun selectCountry(country: Country) {
        country.isSelected = true
        viewModelScope.launch {
            countryRepository.setSelectedCountry(country)
        }
    }

    fun unselectCountry(country: Country) {
        country.isSelected = false
        viewModelScope.launch {
            countryRepository.removeSelectedCountry(country)
        }
    }

    fun findCountry(query: String) {
        viewModelScope.launch {
            countryUiState = try {
                val countries = countryRepository.listOfCountries().first()
                CountryUiState.Success(countries.filter {
                    it.name.contains(
                        query,
                        ignoreCase = true
                    )
                })
            } catch (e: Exception) {
            CountryUiState.Error
        }
        }
    }
}

enum class SearchWidgetState {
    OPENED,
    CLOSED
}

sealed class CountryUiState {
    data class Success(val countries: List<Country>) : CountryUiState()
    object Error: CountryUiState()
    object Loading : CountryUiState()
}