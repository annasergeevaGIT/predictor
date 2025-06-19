package com.rbs.predictor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rbs.predictor.data.PredictionRepository
import com.rbs.predictor.data.PredictionResult
import kotlinx.coroutines.launch

class PredictionViewModel(private val repository: PredictionRepository) : ViewModel() {

    // LiveData to expose the UI state
    private val _prediction = MutableLiveData<PredictionResultState>()
    val prediction: LiveData<PredictionResultState> get() = _prediction

    // Sealed class INSIDE ViewModel to represent the UI state
    sealed class PredictionResultState {
        object Loading : PredictionResultState()
        data class Success(val date: String, val coin: String) : PredictionResultState() // data from PredictionResult.Success
        data class Error(val message: String) : PredictionResultState()           // data from PredictionResult.Error
        data class NotFound(val message: String) : PredictionResultState()         // data from PredictionResult.NotFound
    }

    fun fetchDailyPrediction() {
        _prediction.value = PredictionResultState.Loading // Indicate loading started
        viewModelScope.launch {
            // Call the repository function which returns data.PredictionResult
            when (val result = repository.getDailyPrediction()) {
                is PredictionResult.Success -> {
                    // Map data.PredictionResult.Success to viewmodel.PredictionResultState.Success
                    _prediction.postValue(PredictionResultState.Success(result.date, result.coin))
                }
                is PredictionResult.Error -> {
                    // Map data.PredictionResult.Error to viewmodel.PredictionResultState.Error
                    _prediction.postValue(PredictionResultState.Error(result.message))
                }
                is PredictionResult.NotFound -> {
                    // Map data.PredictionResult.NotFound to viewmodel.PredictionResultState.NotFound
                    _prediction.postValue(PredictionResultState.NotFound(result.message))
                }
            }
        }
    }
}

// Factory to provide the repository to the ViewModel
class PredictionViewModelFactory(private val repository: PredictionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredictionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PredictionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}