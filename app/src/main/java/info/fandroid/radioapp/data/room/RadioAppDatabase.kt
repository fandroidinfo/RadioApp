package info.fandroid.radioapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import info.fandroid.radioapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Country::class], version = 1)
abstract class RadioAppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    class Callback @Inject constructor(
        private val database: Provider<RadioAppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}
