package com.example.booksearch.model

sealed class AppState{
    data class Response(val data: BookResponse): AppState()
    data class Error(val error: String): AppState()
    data class Loading(val isLoading: Boolean) : AppState()
}
