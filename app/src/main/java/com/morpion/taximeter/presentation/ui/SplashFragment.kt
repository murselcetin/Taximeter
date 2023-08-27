package com.morpion.taximeter.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.databinding.FragmentSplashBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.presentation.ui.viewmodel.SplashViewModel
import com.morpion.taximeter.shared.TaxiStandsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                navigateHomePage()
            }
        }
    }

    private fun navigateHomePage() {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        findNavController().navigate(action)
    }

}