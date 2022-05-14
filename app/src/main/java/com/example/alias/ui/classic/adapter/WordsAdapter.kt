package com.example.alias.ui.classic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.databinding.ClassicModeWordItemBinding

class WordsAdapter(
    val onClick: () -> Unit
): RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    var items = mutableListOf<String>()

    inner class ViewHolder(private val binding:ClassicModeWordItemBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(position: Int){
            val model = items[position]
            binding.classicWordTv.text = model
            itemView.setOnClickListener { onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ClassicModeWordItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount() = items.size

    fun setData(items: MutableList<String>){
        this.items = items
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}