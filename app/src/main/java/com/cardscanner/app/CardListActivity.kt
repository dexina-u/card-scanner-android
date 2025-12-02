package com.cardscanner.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardscanner.app.data.CardEntity
import com.cardscanner.app.databinding.ActivityCardListBinding
import com.cardscanner.app.viewmodel.CardViewModel
import java.text.SimpleDateFormat
import java.util.*

class CardListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCardListBinding
    private lateinit var cardViewModel: CardViewModel
    private lateinit var adapter: CardAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]
        
        adapter = CardAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        
        cardViewModel.allCards.observe(this) { cards ->
            adapter.submitList(cards)
            binding.tvEmpty.visibility = if (cards.isEmpty()) View.VISIBLE else View.GONE
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    inner class CardAdapter : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
        
        private var cards = emptyList<CardEntity>()
        
        inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
            val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card, parent, false)
            return CardViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val card = cards[position]
            holder.tvName.text = card.name
            holder.tvAddress.text = card.address
            
            val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            holder.tvTimestamp.text = sdf.format(Date(card.timestamp))
        }
        
        override fun getItemCount() = cards.size
        
        fun submitList(newCards: List<CardEntity>) {
            cards = newCards
            notifyDataSetChanged()
        }
    }
}