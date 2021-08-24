package com.example.booksearch.model

data class BookResponse(
    val items: List<BookItem>
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks,
    val description: String?,
    val publisher: String?
)

data class ImageLinks(
    val thumbnail: String
)