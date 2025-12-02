package com.cardscanner.app.data

import androidx.lifecycle.LiveData

class CardRepository(private val cardDao: CardDao) {
    
    val allCards: LiveData<List<CardEntity>> = cardDao.getAllCards()
    
    suspend fun insert(card: CardEntity): Long {
        return cardDao.insert(card)
    }
    
    suspend fun update(card: CardEntity) {
        cardDao.update(card)
    }
    
    suspend fun delete(card: CardEntity) {
        cardDao.delete(card)
    }
    
    suspend fun deleteAll() {
        cardDao.deleteAll()
    }
    
    suspend fun getCardById(id: Long): CardEntity? {
        return cardDao.getCardById(id)
    }
}