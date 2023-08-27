package com.morpion.taximeter.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.R
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentTaxiFareCalculationBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.shared.TaxiFaresManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaxiFareCalculationFragment :
    BaseFragment<FragmentTaxiFareCalculationBinding>(FragmentTaxiFareCalculationBinding::inflate) {

    @Inject
    lateinit var taxiFaresManager: TaxiFaresManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCitySpinner()

        binding.btnCalculation.setSafeOnClickListener {
            Log.e("TAG", "mesafe: ${binding.edDistance.text} ", )
            Log.e("TAG", "şehir: ${binding.autoCompleteTextView.text} ", )
            if (binding.autoCompleteTextView.text.toString() != "Şehir Seçiniz" && binding.edDistance.text.toString() != "") {
                taxiFaresCalculation(binding.autoCompleteTextView.text.toString(), binding.edDistance.text.toString().toDouble())
            }
        }
    }

    private fun setCitySpinner() {
        val city = ArrayList<String>()

        taxiFaresManager.getTaxiFares().forEach { itTaxiFares ->
            if (itTaxiFares.defaultFee != "0") {
                city.add(itTaxiFares.city ?: "")
            }
        }
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.drop_down_item, city)
        binding.autoCompleteTextView.setAdapter(adapter)
    }

    private fun taxiFaresCalculation(city: String, distance: Double) {
        val cityData = taxiFaresManager.searchCityTaxiFares(city = city)
        if (cityData != null){
            binding.clCalculateInfo.visibility = View.VISIBLE
            binding.tvCityDesc.text = cityData?.info
            if (distance < 3.0) {
                binding.tvPaid.text = String.format("%.2f", cityData?.defaultFee?.toDouble())
                binding.tvDefault.visibility = View.VISIBLE
            } else {
                val taxiFares = (cityData?.startFee?.toDouble()?:0.0) + ((cityData?.feePerKm?.toDouble()?:0.0) * distance)
                binding.tvPaid.text = String.format("%.2f", taxiFares)
                binding.tvDefault.visibility = View.GONE
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateHomeFragment()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun navigateHomeFragment() {
        val action = TaxiFareCalculationFragmentDirections.actionTaxiFareCalculationFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}