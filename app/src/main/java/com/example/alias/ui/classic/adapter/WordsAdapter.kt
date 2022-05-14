package com.example.alias.ui.classic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.R
import com.example.alias.databinding.ClassicModeWordItemBinding

class WordsAdapter(
    val onMark: () -> Unit,
    val onUnMark: () -> Unit,
    val log: () -> Unit
) : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    private val items = mutableListOf<String>()
    private val markedItems = mutableListOf<Boolean>()

    inner class ViewHolder(
        private val binding: ClassicModeWordItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val model = items[position]
            binding.classicWordTv.text = model
            itemView.background = (itemView.resources.getColor(R.color.teal_700)).toDrawable()
            itemView.setOnClickListener {
                if (markedItems[position]) {
                    onUnMark()
                    it.background = (it.resources.getColor(R.color.teal_700)).toDrawable()
                } else {
                    onMark()
                    it.background = (it.resources.getColor(R.color.purple_200)).toDrawable()
                }
                log()
                markedItems[position] = !markedItems[position]
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ClassicModeWordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount() = items.size

    fun setData(items: MutableList<String>) {
        this.items.clear()
        this.items.addAll(items)
        this.markedItems.clear()
        this.markedItems.addAll(items.map { false })
        notifyDataSetChanged()
    }
}