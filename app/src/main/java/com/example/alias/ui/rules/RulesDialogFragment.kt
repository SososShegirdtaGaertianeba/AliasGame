package com.example.alias.ui.rules

import android.os.Bundle
import android.text.method.ArrowKeyMovementMethod
import android.text.method.BaseMovementMethod
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alias.R
import com.example.alias.databinding.FragmentRulesDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RulesDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRulesDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRulesDialogBinding.inflate(inflater, container, false)
//        binding.rulesTv.movementMethod = ScrollingMovementMethod()
        return binding.root

    }

    override fun getTheme(): Int =
        R.style.BottomSheetDialogStyle

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}