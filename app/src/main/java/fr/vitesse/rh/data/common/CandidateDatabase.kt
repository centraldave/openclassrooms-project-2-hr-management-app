package fr.vitesse.rh.data.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.vitesse.rh.data.dao.CandidateDao
import fr.vitesse.rh.data.model.Candidate

@Database(entities = [Candidate::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class CandidateDatabase : RoomDatabase() {
    abstract fun candidateDao(): CandidateDao
}