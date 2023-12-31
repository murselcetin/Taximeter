package com.morpion.taximeter.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morpion.taximeter.util.RestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    fun <T> request(
        flow: Flow<RestResult<T>>,
        onSuccess: ((data: T) -> Unit)? = null,
        onError: (suspend (t: Exception) -> Unit)? = null,
        onLoading: (() -> Unit)? = null
    ) = viewModelScope.launch {
        flow.collect { result ->
            when (result) {
                is RestResult.Loading -> onLoading?.invoke()
                is RestResult.Success -> onSuccess?.invoke(result.data)
                is RestResult.Error -> {
                    onError?.invoke(result.exception)
                }
            }
        }
    }

    fun <T> requestLocal(
        flow: Flow<T>,
        onSuccess: ((data: T) -> Unit)? = null
    ) = viewModelScope.launch {
        flow.collect { result ->
            onSuccess?.invoke(result)
        }
    }
}
