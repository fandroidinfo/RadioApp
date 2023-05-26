package info.fandroid.radioapp.data.room

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
@Immutable
data class Country(
    @PrimaryKey var countryCode: String,
    var name: String,
    var stationCount: Int,
    var isSelected: Boolean
)