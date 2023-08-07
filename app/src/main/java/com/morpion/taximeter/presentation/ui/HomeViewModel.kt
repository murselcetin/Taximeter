package com.morpion.taximeter.presentation.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.domain.use_case.GetLastTaximeterHistoryUseCase
import com.morpion.taximeter.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLastTaximeterHistoryUseCase: GetLastTaximeterHistoryUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun getLastTaximeterHistory() {
        viewModelScope.launch {
            getLastTaximeterHistoryUseCase.getLastTaximeterHistory().collectLatest {
                _uiState.update { uiState->
                    uiState.copy(
                        data = it
                    )
                }
            }
        }
    }

    data class UiState(
        val data: List<TaximeterHistoryLocalData> = emptyList()
    )
}