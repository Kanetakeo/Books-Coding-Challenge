package com.example.booksearch.model

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getBookNames(bookName: String): Flow<AppState>
}