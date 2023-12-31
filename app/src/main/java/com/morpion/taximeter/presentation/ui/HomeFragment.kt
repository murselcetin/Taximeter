package com.morpion.taximeter.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.morpion.taximeter.common.extensions.safeNavigate
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.data.local.entity.toUiModel
import com.morpion.taximeter.databinding.FragmentHomeBinding
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.presentation.ui.adapter.LastTaximeterHistoryAdapter
import com.morpion.taximeter.presentation.ui.viewmodel.HomeViewModel
import com.morpion.taximeter.util.LocalSessions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var sessions: LocalSessions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        setupLastTaximeterHistoryAdapter()

        binding.clTaximeter.setSafeOnClickListener {
            navigateTaximeterFragment()
        }

        binding.tvAllTaximeterHistory.setSafeOnClickListener {
            navigateTaximeterHistoryFragment()
        }

        binding.clDirection.setSafeOnClickListener {
            navigateDirectionsFragment()
        }

        binding.clTaxiStands.setSafeOnClickListener {
            navigateTaxiStandsFragment()
        }

        binding.clPaidCalculation.setSafeOnClickListener {
            navigateTaxiFareCalculationFragment()
        }

        binding.ivSettings.setSafeOnClickListener {
            navigateSettingsFragment()
        }

        viewModel.getLastTaximeterHistory()
    }

    private fun navigateSettingsFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
        findNavController().safeNavigate(action)
    }

    private fun navigateTaxiFareCalculationFragment() {
        sessions.distance = "0,0 km"
        val action = HomeFragmentDirections.actionHomeFragmentToTaxiFareCalculationFragment()
        findNavController().safeNavigate(action)
    }

    private fun navigateTaximeterHistoryFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToTaximeterHistoryFragment()
        findNavController().safeNavigate(action)
    }

    private fun navigateTaximeterFragment(){
        val action = HomeFragmentDirections.actionHomeFragmentToTaximeterFragment()
        findNavController().safeNavigate(action)
    }

    private fun navigateDirectionsFragment(){
        val action = HomeFragmentDirections.actionHomeFragmentToDirectionsFragment()
        findNavController().safeNavigate(action)
    }

    private fun navigateTaxiStandsFragment(){
        val action = HomeFragmentDirections.actionHomeFragmentToTaxiStandsFragment()
        findNavController().safeNavigate(action)
    }

    private fun setupLastTaximeterHistoryAdapter() {
        binding.rvTaximeterHistory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTaximeterHistory.adapter = LastTaximeterHistoryAdapter()
    }

    private fun observe() = with(viewModel) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    uiState.collectLatest { uiState ->
                        val lastTaximeterHistory = uiState.data
                        val lastTaximeterHistoryList = ArrayList<TaximeterHistoryUIModel>()
                        lastTaximeterHistory.forEach { itTaximeter ->
                            lastTaximeterHistoryList.add(itTaximeter.toUiModel())
                        }
                        if (lastTaximeterHistoryList.isNotEmpty()) {
                            binding.clTaximeterHistory.visibility = View.VISIBLE
                            (binding.rvTaximeterHistory.adapter as LastTaximeterHistoryAdapter).submitList(lastTaximeterHistoryList)
                        } else {
                            binding.clTaximeterHistory.visibility = View.GONE
                            (binding.rvTaximeterHistory.adapter as LastTaximeterHistoryAdapter).submitList(emptyList())
                        }
                    }
                }
            }

        }
    }

}