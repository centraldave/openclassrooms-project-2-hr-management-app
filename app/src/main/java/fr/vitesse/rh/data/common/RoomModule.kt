package fr.vitesse.rh.data.common

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.vitesse.rh.data.dao.CandidateDao
import fr.vitesse.rh.data.repository.CandidateRepository
import fr.vitesse.rh.data.service.CandidateService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideCandidateDatabase(@ApplicationContext context: Context): CandidateDatabase {
        return Room.databaseBuilder(
            context,
            CandidateDatabase::class.java,
            "candidate_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCandidateDao(database: CandidateDatabase): CandidateDao {
        return database.candidateDao()
    }

    @Provides
    @Singleton
    fun provideCandidateRepository(candidateDao: CandidateDao): CandidateRepository {
        return CandidateService(candidateDao)
    }
}