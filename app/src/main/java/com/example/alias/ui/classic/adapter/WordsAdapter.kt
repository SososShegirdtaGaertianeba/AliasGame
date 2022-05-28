package com.example.alias.ui.classic.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.R
import com.example.alias.databinding.ClassicModeWordItemBinding

class WordsAdapter(
    val onMark: () -> Unit,
    val onUnMark: () -> Unit,
) : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    private val items = mutableListOf<String>()
    private val markedItems = mutableListOf<Boolean>()

    inner class ViewHolder(
        private val binding: ClassicModeWordItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private fun setResource(position: Int) = when {
            position == 0 && markedItems[position] -> itemView.setBackgroundResource(R.drawable.classic_adapter_item_top_rounded_shape_marked)
            position == 0 -> itemView.setBackgroundResource(R.drawable.classic_adapter_item_top_rounded_shape_unmarked)
            position == items.size - 1 && markedItems[position] -> itemView.setBackgroundResource(R.drawable.classic_adapter_item_bottom_rounded_shape_marked)
            position == items.size - 1 -> itemView.setBackgroundResource(R.drawable.classic_adapter_item_bottom_rounded_shape_unmarked)
            markedItems[position] -> itemView.setBackgroundResource(R.drawable.gradient_rect)
            else -> itemView.setBackgroundResource(R.color.white_2)
        }

        fun onBind(position: Int) {
            binding.classicWordTv.setTextColor(Color.parseColor("#2E3440"))
            val model = items[position]
            setResource(position)
            binding.classicWordTv.text = model
            itemView.setOnClickListener {
                if (markedItems[position]) {
                    onUnMark()
                    binding.classicWordTv.setTextColor(Color.parseColor("#2E3440"))
                } else {
                    onMark()
                    binding.classicWordTv.setTextColor(Color.parseColor("#E1ECF0"))
                }

                markedItems[position] = !markedItems[position]
                setResource(position)
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