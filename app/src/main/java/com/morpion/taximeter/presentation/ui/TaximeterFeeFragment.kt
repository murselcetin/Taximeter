package com.morpion.taximeter.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.common.extensions.safeNavigate
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentTaximeterFeeBinding
import com.morpion.taximeter.presentation.base.BaseFragment
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
            if (binding.edStartFee.text.toString() != "" && binding.edPerKmFee.text.toString() != "") {
                updateTaximeterFee()
            } else {
                if (binding.edStartFee.text.toString() == "") {
                    alertDialog("Lütfen Taksimetre Açılış Ücreti Giriniz!",type = 0)
                } else {
                    alertDialog("Lütfen 1 km Mesafe Ücreti Giriniz!",type = 0)
                }
            }
        }
    }

    private fun updateTaximeterFee() {
        localSessions.taximeterStartPrice = binding.edStartFee.text.toString()
        localSessions.taximeterKmPrice = binding.edPerKmFee.text.toString()
        alertDialog("Güncellendi!", type = 1)
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
        findNavController().safeNavigate(action)
    }
}