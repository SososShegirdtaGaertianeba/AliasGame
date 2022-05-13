package com.example.alias.ui.home

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.example.alias.MainActivity
import com.example.alias.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.alias.R
import com.example.alias.databinding.FragmentHomeBinding
import com.example.alias.ui.base.BaseFragment
import com.example.alias.util.LocaleUtils

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var isSpinnerInitialized = false

    override fun init() {
        binding.chooseLang.setSelection(getSharedPreference().getInt(PREFERENCE_NAME, PREFERENCE_DEFAULT_VALUE))
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
        resources.getStringArray(R.array.language_array)[ZERO] -> EN
        resources.getStringArray(R.array.language_array)[ONE] -> RU
        resources.getStringArray(R.array.language_array)[TWO] -> KA
        else -> EN
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
                    setSharedPreference(
                        LocaleUtils.getIdFromLanguage(
                            toLocaleString(
                                adapterView.getItemAtPosition(
                                    position
                                ).toString()
                            )
                        )
                    )
                    startActivity(Intent(context, MainActivity::class.java))
                } else isSpinnerInitialized = true
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                adapterView?.getItemAtPosition(ZERO)
            }

        }
    }

    private fun getSharedPreference() =
        requireActivity().getSharedPreferences(
            SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )

    fun setSharedPreference(language: Int) =
        getSharedPreference()
            .edit().putInt(PREFERENCE_NAME, language).apply()

    companion object {
        const val PREFERENCE_NAME = "language"
        const val PREFERENCE_DEFAULT_VALUE = 0
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2
        private const val EN = "en"
        private const val RU = "ru"
        private const val KA = "ka"
    }

}
