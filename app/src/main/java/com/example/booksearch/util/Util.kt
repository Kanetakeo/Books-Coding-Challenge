package com.example.booksearch.util

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.booksearch.R
import com.example.booksearch.view.BookDetailFragment
import com.example.booksearch.view.BookListFragment


fun FragmentActivity?.showError(error: String) {
    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
}

fun FragmentActivity.hideShowProgressBar(isLoading: Boolean){
    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
    if (isLoading)
        progressBar.visibility = View.VISIBLE
    else
        progressBar.visibility = View.GONE
}

fun FragmentActivity.openDetailFragment(position: Int){
    supportFragmentManager.beginTransaction()
        .replace(R.id.container, BookDetailFragment.bookDetail(position))
        .addToBackStack(null)
        .commit()
}

fun FragmentActivity.navigateBookListFragment(){
    supportFragmentManager.beginTransaction()
        .replace(R.id.container, BookListFragment())
        .commit()
}