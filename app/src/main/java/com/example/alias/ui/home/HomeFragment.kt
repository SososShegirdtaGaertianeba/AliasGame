package com.example.alias.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.example.alias.MainActivity
import com.example.alias.R
import com.example.alias.app.App
import com.example.alias.databinding.FragmentHomeBinding
import com.example.alias.ui.base.BaseFragment
import com.example.alias.util.LocaleUtils

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var isSpinnerInitialized = false

    override fun init() {
        binding.chooseLang.setSelection(App.LANGUAGE)
        navigateToConfigurationFragment()
        navigateToRulesFragment()
        chooseLanguage()
    }

    private fun navigateToRulesFragment() {
        binding.rulesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rulesDialogFragment)
        }
    }

    private fun navigateToConfigurationFragment() {
        binding.confBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_configureFragment)
        }
    }

    fun toLocaleString(language: String) = when (language) {
        resources.getStringArray(R.array.language_array)[0] -> "en"
        resources.getStringArray(R.array.language_array)[1] -> "ru"
        resources.getStringArray(R.array.language_array)[2] -> "ka"
        else -> "en"
    }

    private fun chooseLanguage() {
        binding.chooseLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (isSpinnerInitialized) {
                    App.LANGUAGE =
                        LocaleUtils.getIdFromLanguage(
                            toLocaleString(
                                adapterView.getItemAtPosition(
                                    position
                                ).toString()
                            )
                        )
                    startActivity(Intent(context, MainActivity::class.java))
                } else isSpinnerInitialized = true
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                adapterView?.getItemAtPosition(0)
            }

        }
    }

}
