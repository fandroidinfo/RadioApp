package info.fandroid.radioapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import info.fandroid.radioapp.data.room.CountryDao
import info.fandroid.radioapp.data.room.RadioAppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: RadioAppDatabase.Callback): RadioAppDatabase{
        return Room.databaseBuilder(application, RadioAppDatabase::class.java, "radio_app_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideArticleDao(db: RadioAppDatabase): CountryDao {
        return db.countryDao()
    }
}