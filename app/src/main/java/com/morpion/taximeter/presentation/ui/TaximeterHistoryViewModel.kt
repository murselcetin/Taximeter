package com.morpion.taximeter.presentation.ui

import androidx.lifecycle.viewModelScope
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.domain.use_case.DeleteTaximeterUseCase
import com.morpion.taximeter.domain.use_case.GetTaximeterHistoryUseCase
import com.morpion.taximeter.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaximeterHistoryViewModel @Inject constructor(
    private val getTaximeterHistoryUseCase: GetTaximeterHistoryUseCase,
    private val deleteTaximeterUseCase: DeleteTaximeterUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun getTaximeterHistory() {
        viewModelScope.launch {
            getTaximeterHistoryUseCase.getTaximeterHistory().collectLatest {
                _uiState.update { uiState->
                    uiState.copy(
                        data = it
                    )
                }
            }
        }
    }

    fun deleteTaximeter(id: Int) {
        viewModelScope.launch {
            deleteTaximeterUseCase.deleteTaximeter(id)
        }
    }

    data class UiState(
        val data: List<TaximeterHistoryLocalData> = emptyList()
    )
}