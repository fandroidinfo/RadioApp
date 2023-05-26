package info.fandroid.radioapp.data.network

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("name"         ) var name: String? = null,
    @SerializedName("iso_3166_1"   ) var countryCode: String? = null,
    @SerializedName("stationcount" ) var stationCount: Int?    = null
)
