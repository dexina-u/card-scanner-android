package com.cardscanner.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY timestamp DESC")
    fun getAllCards(): LiveData<List<CardEntity>>
    
    @Insert
    suspend fun insert(card: CardEntity): Long
    
    @Update
    suspend fun update(card: CardEntity)
    
    @Delete
    suspend fun delete(card: CardEntity)
    
    @Query("DELETE FROM cards")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCardById(id: Long): CardEntity?
}