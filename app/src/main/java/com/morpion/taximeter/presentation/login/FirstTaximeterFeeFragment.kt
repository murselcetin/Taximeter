package com.morpion.taximeter.presentation.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.common.extensions.safeNavigate
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentFirstTaximeterFeeBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.util.LocalSessions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirstTaximeterFeeFragment : BaseFragment<FragmentFirstTaximeterFeeBinding>(FragmentFirstTaximeterFeeBinding::inflate) {

    @Inject
    lateinit var localSessions: LocalSessions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveFee.setSafeOnClickListener {
            if (binding.edStartFee.text.toString() != "" && binding.edPerKmFee.text.toString() != "") {
                saveTaximeterFee()
            } else {
                if (binding.edStartFee.text.toString() == "") {
                    alertDialog("Lütfen Taksimetre Açılış Ücreti Giriniz!", type = 0)
                } else {
                    alertDialog("Lütfen 1 km Mesafe Ücreti Giriniz!",type = 0)
                }
            }
        }

        if (localSessions.firstTaximeterFee) {
            navigateSplashFragment()
        }
    }

    private fun saveTaximeterFee() {
        localSessions.taximeterStartPrice = binding.edStartFee.text.toString()
        localSessions.taximeterKmPrice = binding.edPerKmFee.text.toString()
        navigateSplashFragment()
    }

    private fun navigateSplashFragment() {
        localSessions.firstTaximeterFee = true
        val action = FirstTaximeterFeeFragmentDirections.actionFirstTaximeterFeeFragmentToSplashFragment()
        findNavController().safeNavigate(action)
    }
}