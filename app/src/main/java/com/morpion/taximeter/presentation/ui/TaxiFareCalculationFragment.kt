package com.morpion.taximeter.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.morpion.taximeter.R
import com.morpion.taximeter.common.extensions.kmToDouble
import com.morpion.taximeter.common.extensions.safeNavigate
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentTaxiFareCalculationBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.shared.TaxiFaresManager
import com.morpion.taximeter.util.LocalSessions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaxiFareCalculationFragment : BaseFragment<FragmentTaxiFareCalculationBinding>(FragmentTaxiFareCalculationBinding::inflate) {

    @Inject
    lateinit var taxiFaresManager: TaxiFaresManager

    @Inject
    lateinit var sessions: LocalSessions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCitySpinner()

        if (sessions.distance?.kmToDouble() != "0.0") {
            binding.edDistance.setText(sessions.distance?.kmToDouble())
        }

        binding.btnCalculation.setSafeOnClickListener {
            if (binding.autoCompleteTextView.text.toString() != "Şehir Seçiniz" && binding.edDistance.text.toString() != "") {
                taxiFaresCalculation(binding.autoCompleteTextView.text.toString(), binding.edDistance.text.toString().toDouble())
            } else {
                if (binding.autoCompleteTextView.text.toString() == "Şehir Seçiniz") {
                    alertDialog("Lütfen Şehir Seçiniz!",type = 0)
                }else {
                    alertDialog("Lütfen Mesafe Giriniz!",type = 0)
                }
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
        findNavController().safeNavigate(action)
    }
}