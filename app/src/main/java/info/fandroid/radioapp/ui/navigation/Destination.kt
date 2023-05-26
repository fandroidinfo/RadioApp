package info.fandroid.radioapp.ui.navigation

object Destination {
    const val favorites = "favorites"
    const val radioScreen = "radio"
    const val home = "home"
    const val countries = "countries"
    const val radioStation = "station/{id}"

    fun station(id: String): String = "station/$id"
}