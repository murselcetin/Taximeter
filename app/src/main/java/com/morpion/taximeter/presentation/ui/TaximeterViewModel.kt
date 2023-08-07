package com.morpion.taximeter.presentation.ui

import androidx.lifecycle.viewModelScope
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.domain.use_case.SaveTaximeterUseCase
import com.morpion.taximeter.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaximeterViewModel @Inject constructor(
    private val saveTaximeterUseCase: SaveTaximeterUseCase
) : BaseViewModel() {

    fun saveTaximeter(taximeterData: TaximeterHistoryLocalData) {
        viewModelScope.launch {
            saveTaximeterUseCase.saveTaximeter(taximeterData)
        }
    }

}