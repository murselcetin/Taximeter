package com.morpion.taximeter.presentation.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.R
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentTaximeterFeeBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.shared.TaxiFaresManager
import com.morpion.taximeter.util.LocalSessions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaximeterFeeFragment : BaseFragment<FragmentTaximeterFeeBinding>(FragmentTaximeterFeeBinding::inflate) {

    @Inject
    lateinit var localSessions: LocalSessions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdateFee.setSafeOnClickListener {
            updateTaximeterFee()
        }
    }

    private fun updateTaximeterFee() {
        localSessions.taximeterStartPrice = binding.edStartFee.text.toString()
        localSessions.taximeterKmPrice = binding.edPerKmFee.text.toString()
        Toast.makeText(requireContext(), "Güncellendi", Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateSettingFragment()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun navigateSettingFragment() {
        val action = TaximeterFeeFragmentDirections.actionTaximeterFeeFragmentToSettingsFragment()
        findNavController().navigate(action)
    }
}