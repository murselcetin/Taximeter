package com.morpion.taximeter.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.data.local.entity.toUiModel
import com.morpion.taximeter.databinding.FragmentHomeBinding
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.presentation.ui.adapter.LastTaximeterHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

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

        viewModel.getLastTaximeterHistory()
    }

    private fun navigateTaximeterHistoryFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToTaximeterHistoryFragment()
        findNavController().navigate(action)
    }

    private fun navigateTaximeterFragment(){
        val action = HomeFragmentDirections.actionHomeFragmentToTaximeterFragment()
        findNavController().navigate(action)
    }

    private fun navigateDirectionsFragment(){
        val action = HomeFragmentDirections.actionHomeFragmentToDirectionsFragment()
        findNavController().navigate(action)
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