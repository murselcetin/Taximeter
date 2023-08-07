package com.morpion.taximeter.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentHomeBinding
import com.morpion.taximeter.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clTaximeter.setSafeOnClickListener {
            navigateTaximeterFragment()
        }

        taximeterHistoryNullControl()

    }

    private fun navigateTaximeterFragment(){
        val action = HomeFragmentDirections.actionHomeFragmentToTaximeterFragment()
        findNavController().navigate(action)
    }

    private fun taximeterHistoryNullControl(){
        if (binding.rvTaximeterHistory.adapter?.itemCount == null) {
            binding.clTaximeterHistory.visibility = View.GONE
        }else {
            binding.clTaximeterHistory.visibility = View.VISIBLE

        }
    }

}