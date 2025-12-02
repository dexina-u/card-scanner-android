package com.cardscanner.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cardscanner.app.data.CardDatabase
import com.cardscanner.app.data.CardEntity
import com.cardscanner.app.data.CardRepository
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: CardRepository
    val allCards: LiveData<List<CardEntity>>
    
    init {
        val cardDao = CardDatabase.getDatabase(application).cardDao()
        repository = CardRepository(cardDao)
        allCards = repository.allCards
    }
    
    fun saveCard(name: String, address: String, rawText: String) = viewModelScope.launch {
        val card = CardEntity(
            name = name,
            address = address,
            rawText = rawText
        )
        repository.insert(card)
    }
    
    fun deleteCard(card: CardEntity) = viewModelScope.launch {
        repository.delete(card)
    }
    
    fun deleteAllCards() = viewModelScope.launch {
        repository.deleteAll()
    }
}