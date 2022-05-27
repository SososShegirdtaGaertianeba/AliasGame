package com.example.alias.ui.score_break

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alias.R
import com.example.alias.databinding.FragmentScoreBreakBinding
import com.example.alias.di.viewModels
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.classic.vm.ClassicViewModel
import com.example.alias.ui.score_break.adapter.TeamScoreBreakAdapter
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules

class ScoreBreakFragment : DialogFragment() {
    private var _binding: FragmentScoreBreakBinding? = null
    val binding get() = _binding!!
    private val safeArgs: ScoreBreakFragmentArgs by navArgs()

    private val arcadeViewModel: ArcadeViewModel by inject()
    private val classicViewModel: ClassicViewModel by inject()


    private val adapter by lazy {
        TeamScoreBreakAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBreakBinding.inflate(inflater, container, false)
        loadKoinModules(viewModels)
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (safeArgs.isClassic)
            classicViewModel.toggleIsNextTurn()
        else
            arcadeViewModel.toggleIsNextTurn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBtnContinue()
        if (safeArgs.isClassic)
            adapter.setData(classicViewModel.teamsTotal)
        else
            adapter.setData(arcadeViewModel.teamsTotal)
    }

    private fun initBtnContinue() {
        binding.btnContinue.setText(getString(R.string.continue_game))
        binding.btnContinue.setDrawable(R.drawable.ic_arrow_right)
        binding.btnContinue.setOnClickListener {
            this.dismiss()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}