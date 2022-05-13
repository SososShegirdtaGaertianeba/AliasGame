package com.example.alias.ui.configure.view_pager_fragments.pager_teams.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.databinding.TeamsAdapterItemBinding

class TeamsAdapter : RecyclerView.Adapter<TeamsAdapter.ViewHolder>() {
    val teams = mutableListOf(TEAM_1, TEAM_2)

    inner class ViewHolder(private val binding: TeamsAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind() {
            binding.btnDelete.visibility = View.INVISIBLE
        }

        fun setEditedListener(position: Int) {
            binding.etTeam.addTextChangedListener {
                if (position < itemCount)
                    teams[position] = it.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        TeamsAdapterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
        holder.setEditedListener(position)
    }

    override fun getItemCount(): Int =
        teams.size

    fun addTeam() {
        teams.add("$TEAM_DEFAULT_NAME${itemCount}")
        notifyItemInserted(itemCount)
    }

    companion object {
        private const val TEAM_DEFAULT_NAME = "Team"
        private const val TEAM_1 = "${TEAM_DEFAULT_NAME}1"
        private const val TEAM_2 = "${TEAM_DEFAULT_NAME}2"
    }
}
