package com.example.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.booksearch.model.AppState
import com.example.booksearch.model.Repository
import kotlinx.coroutines.flow.*

private const val TAG = "BookViewModel"

class BookViewModel(private val repository: Repository) : ViewModel() {
    val _inputStateFlow = MutableStateFlow<String>("")
    private val inputStateFlow: StateFlow<String> get() = _inputStateFlow

    val book: LiveData<AppState> = liveData {
        Log.d(TAG, "LiveDataBuilder: ")
        emit(AppState.Loading(true))
        inputStateFlow
            .debounce(500)
            .filter { it.isNotEmpty() }
            .collect { input ->
                repository.getBookNames(
                    input
                ).collect { appState ->
                    emit(AppState.Loading(false))
                    Log.d(TAG, "Collect AppState: $appState")
                    emit(appState)
                }
            }
    }
}