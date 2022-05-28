package com.example.alias.ui.ensure_in_exit

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alias.R
import com.example.alias.databinding.EnsureInExitDialogFragmentBinding
import com.example.alias.extensions.safeNavigate
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.classic.vm.ClassicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.system.measureTimeMillis

class EnsureInExitDialogFragment : BottomSheetDialogFragment() {

    private var _binding: EnsureInExitDialogFragmentBinding? = null
    val binding get() = _binding!!

    private val safeArgs: EnsureInExitDialogFragmentArgs by navArgs()

    private val arcadeViewModel: ArcadeViewModel by inject()
    private val classicViewModel: ClassicViewModel by inject()


    private var isYesClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnsureInExitDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.noBtn.setOnClickListener { dismiss() }
        binding.yesBtn.setOnClickListener {
            isYesClicked = true
            dismiss()
        }

        binding.forfeitGameBtn.setOnClickListener {
            val action =
                EnsureInExitDialogFragmentDirections.actionEnsureInExitDialogFragmentToConfigureFragment()
            findNavController().safeNavigate(action)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        val signalCode = if (isYesClicked) 3 else 2
        if (safeArgs.isClassic) {
            classicViewModel.dismissDuration =
                measureTimeMillis { super.onDismiss(dialog) }
            classicViewModel.handleBackPress(signalCode)
        } else {
            arcadeViewModel.dismissDuration =
                measureTimeMillis { super.onDismiss(dialog) }
            arcadeViewModel.handleBackPress(signalCode)
        }
    }

    override fun getTheme(): Int =
        R.style.BottomSheetDialogStyle

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}