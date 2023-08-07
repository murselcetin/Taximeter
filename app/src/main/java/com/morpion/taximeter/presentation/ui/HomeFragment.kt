package com.morpion.taximeter.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.data.local.entity.toUiModel
import com.morpion.taximeter.databinding.FragmentHomeBinding
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.presentation.ui.adapter.TaximeterHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clTaximeter.setSafeOnClickListener {
            navigateTaximeterFragment()
        }

        viewModel.getLastTaximeterHistory()

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
                            //(binding.rvTaximeterHistory.adapter as TaximeterHistoryAdapter).addItem(mealList)

                        } else {
                            //(binding.rvTaximeterHistory.adapter as TaximeterHistoryAdapter).submitList(emptyList())
                        }
                    }
                }
            }

        }
    }

}