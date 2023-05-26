package info.fandroid.radioapp.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<Country>>

    @Query("SELECT COUNT(*) FROM countries")
    fun getCountOfCountries(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: Country)

    @Delete
    suspend fun deleteCountry(country: Country)
}
