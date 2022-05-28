package com.example.alias.ui.score_break

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alias.R
import com.example.alias.databinding.FragmentScoreBreakBinding
import com.example.alias.di.viewModels
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.classic.vm.ClassicViewModel
import com.example.alias.ui.score_break.adapter.TeamScoreBreakAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules

class ScoreBreakFragment : BottomSheetDialogFragment() {
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
        if (safeArgs.isStartNextTeamRoundRequired) {
            if (safeArgs.isClassic)
                classicViewModel.toggleIsNextTurn()
            else
                arcadeViewModel.toggleIsNextTurn()
        }
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
        if (safeArgs.isStartNextTeamRoundRequired) {
            binding.btnContinue.visibility = View.VISIBLE
            binding.btnContinue.setBtnColor(R.drawable.green_circle_btn_shape)
            binding.btnContinue.setText(getString(R.string.start_next_round))
            binding.btnContinue.setDrawable(R.drawable.ic_arrow_right)
            binding.btnContinue.setOnClickListener {
                this.dismiss()
            }
        } else binding.btnContinue.visibility = View.GONE
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun getTheme(): Int =
        R.style.BottomSheetDialogStyle

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}