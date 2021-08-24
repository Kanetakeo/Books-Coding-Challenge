package com.example.booksearch.model

import android.util.Log
import com.example.booksearch.model.remote.BookApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

private const val TAG = "RepositoryImpl"

class RepositoryImpl(private val api: BookApi, private val dispatcher: CoroutineContext): Repository {
    override suspend fun getBookNames(bookName: String): Flow<AppState> {
        return flow {
            Log.d(TAG, "getBookNames: $bookName")
            val response = api.getBooksByName(bookName)
            if (response.isSuccessful && response.body() != null){
                emit(
                    AppState.Response(response.body()!!)
                )
            }else
                emit(
                    AppState.Error(response.message())
                )
        }.flowOn(dispatcher)
    }
}