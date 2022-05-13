package com.example.alias.ui.home

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.alias.R
import com.example.alias.app.App
import com.example.alias.databinding.FragmentHomeBinding
import com.example.alias.ui.base.BaseFragment
import org.koin.dsl.koinApplication

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun init() {
        navigateToConfigurationFragment()
        navigateToRulesFragment()
        chooseLanguage()
    }

    private fun navigateToRulesFragment(){
        binding.rulesBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_rulesDialogFragment)
        }
    }

    private fun navigateToConfigurationFragment(){
        binding.confBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_configureFragment)
        }
    }


    private fun chooseLanguage(){
        binding.chooseLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                App.LANGUAGE = adapterView.getItemAtPosition(position).toString()
                Log.d("LANGUAGE", App.LANGUAGE)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                adapterView?.getItemAtPosition(0)
            }

        }
    }

}
