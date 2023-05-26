package info.fandroid.radioapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import info.fandroid.radioapp.R
import info.fandroid.radioapp.data.room.Country
import info.fandroid.radioapp.ui.common.SearchAppBar
import info.fandroid.radioapp.ui.viewmodels.CountryViewModel
import info.fandroid.radioapp.ui.viewmodels.CountryUiState
import info.fandroid.radioapp.ui.viewmodels.SearchWidgetState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CountryScreen() {
    val viewModel: CountryViewModel = hiltViewModel()
    val countryUiState = viewModel.countryUiState
    val countOfSelectedCountries by viewModel.countOfSelectedCountries.collectAsState(initial = 0)
    val searchWidgetState = viewModel.searchWidgetState
    val searchTextState = viewModel.searchTextState

    when (countryUiState) {
        is CountryUiState.Loading -> LoadingScreen()
        is CountryUiState.Success -> CountryListScreen(
            viewModel,
            countryUiState,
            countOfSelectedCountries,
            searchWidgetState,
            searchTextState
        )
        is CountryUiState.Error -> ErrorScreen(retryAction = { viewModel.getCountries() })
    }
}

@Composable
fun CountryListScreen(
    viewModel: CountryViewModel,
    countryUiState: CountryUiState,
    countOfSelectedCountries: Int,
    searchWidgetState: SearchWidgetState,
    searchTextState: String
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchAppBar(
                itemsCount = countOfSelectedCountries,
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                    viewModel.findCountry(it)
                },
                onCloseClicked = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                    viewModel.getCountries()
                },
                onSearchClicked = {
                    viewModel.findCountry(it)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                }
            )
        }
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it),
            color = MaterialTheme.colors.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                when (countryUiState) {
                    is CountryUiState.Loading -> LoadingScreen()
                    is CountryUiState.Success -> CountryList(
                        countries = countryUiState.countries,
                        onCountrySelected = { country -> viewModel.selectCountry(country) },
                        onCountryUnselected = { country -> viewModel.unselectCountry(country) }
                    )
                    is CountryUiState.Error -> ErrorScreen(retryAction = { viewModel.getCountries() })
                }
            }
        }
    }
}

@Composable
fun CountryList(
    countries: List<Country>,
    onCountrySelected: (Country) -> Unit,
    onCountryUnselected: (Country) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    ) {
        items(
            items = countries,
            key = {
                country ->
                country.countryCode
        }) {  country ->
            CountryListItem(
                country = country,
                onCountrySelected = onCountrySelected,
                onCountryUnselected = onCountryUnselected
            )
        }
    }
}

@Composable
fun CountryListItem(
    country: Country,
    onCountrySelected: (Country) -> Unit,
    onCountryUnselected: (Country) -> Unit
) {

    val isCheckedState = remember { mutableStateOf(country.isSelected) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = isCheckedState.value,
                onCheckedChange = { isChecked ->
                    isCheckedState.value = isChecked
                    if (isChecked) {
                        onCountrySelected(country)
                    } else {
                        onCountryUnselected(country)
                    }
                }
            )
            Column {
                Text(
                    text = country.name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.radio_station_count, country.stationCount),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun CountryListItemPreview() {
    CountryListItem(
        country = Country("US","United States", 13500, false),
        onCountrySelected = {},
        onCountryUnselected = {}
    )
}

@Preview
@Composable
fun CountryListPreview() {
    val countries = listOf(
        Country("US", "United States", 2000, false),
        Country("CA", "Canada", 1500, true),
        Country("MX", "Mexico", 1000, false),
        Country("BR", "Brazil", 500, true),
        Country("AR", "Argentina", 250, false),
        Country("CO", "Colombia", 100, false),
    )
    CountryList(
        countries = countries,
        onCountrySelected = {},
        onCountryUnselected = {}
    )
}
