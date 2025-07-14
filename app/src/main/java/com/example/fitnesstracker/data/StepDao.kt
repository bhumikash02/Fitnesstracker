package com.example.fitnesstracker.data
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fitnesstracker.model.StepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    /**
     * Inserts a new daily step record if the date doesn't exist,
     * or updates the existing record if it does. This is perfect for
     * repeatedly saving today's step count.
     */
    @Upsert
    suspend fun upsert(stepEntity: StepEntity)

    /**
     * Retrieves the step records for the last 7 days, ordered from most recent to oldest.
     * This Flow will automatically update the UI whenever the data changes.
     */
    @Query("SELECT * FROM daily_steps ORDER BY date DESC LIMIT 7")
    fun getStepsForLastSevenDays(): Flow<List<StepEntity>>
}