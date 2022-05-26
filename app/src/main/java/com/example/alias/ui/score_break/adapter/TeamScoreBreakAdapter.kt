package com.example.alias.ui.score_break.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.databinding.ScoreBreakRecyclerItemBinding

class TeamScoreBreakAdapter : RecyclerView.Adapter<TeamScoreBreakAdapter.ViewHolder>() {

    private var teamMap = mutableMapOf<String, Int>()
    private var teamList = ArrayList<Pair<String, Int>>()

    inner class ViewHolder(private val binding: ScoreBreakRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val model = teamList[position]
            with(binding) {
                teamNameTV.text = model.first
                teamScoreTV.text = model.second.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ScoreBreakRecyclerItemBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount() = teamList.size

    fun setData(teamMap: Map<String, Int>) {
        this.teamMap.clear()
        this.teamMap = teamMap.toMutableMap()
        this.teamList.clear()
        this.teamList.addAll(teamMap.entries.sortedBy { -it.value }.map { Pair(it.key, it.value) })
        notifyDataSetChanged()
    }

}