package com.example.booksearch.model.remote

import com.example.booksearch.model.BookResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("books/v1/volumes")
    suspend fun getBooksByName(
        @Query("q")
        bookName: String = "bible"
    ): Response<BookResponse>

    companion object{
        fun getApi() =
            Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookApi::class.java)
    }
}