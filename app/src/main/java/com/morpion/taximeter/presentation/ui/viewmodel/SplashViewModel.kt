package com.morpion.taximeter.presentation.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.morpion.taximeter.domain.use_case.GetTaxiStandsUseCase
import com.morpion.taximeter.presentation.base.BaseViewModel
import com.morpion.taximeter.shared.TaxiStandsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTaxiStandsUseCase: GetTaxiStandsUseCase,
    private val taxiStandsManager: TaxiStandsManager,
) : BaseViewModel() {

    val success = MutableLiveData<Boolean>()

    var timeout: Int = 0
    private val timeoutControl = MutableLiveData<Boolean>()

    init {
        checkData()
    }

    private fun checkData() {
        getTaxiStandsList {
            success.value = true
        }
    }

    private fun getTaxiStandsList(data: () -> Unit) {
        viewModelScope.launch {
            request(
                flow = getTaxiStandsUseCase(),
                onSuccess = { itTaxiStands ->
                    taxiStandsManager.setTaxiStandList(itTaxiStands)
                    data.invoke()
                },
                onError = {
                    if (timeout > 5) {
                        timeoutControl.value = true
                    } else {
                        checkData()
                    }
                    timeout++
                }
            )
        }
    }
}