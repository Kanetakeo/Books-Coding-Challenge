package com.example.booksearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import com.example.booksearch.databinding.ActivityMainBinding
import com.example.booksearch.databinding.BookToolbarBinding
import com.example.booksearch.model.Repository
import com.example.booksearch.model.RepositoryImpl
import com.example.booksearch.model.remote.BookApi
import com.example.booksearch.util.navigateBookListFragment
import com.example.booksearch.view.BookListFragment
import com.example.booksearch.viewmodel.BookViewModel
import com.example.booksearch.viewmodel.provider.BookViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private lateinit var toolbarBinding: BookToolbarBinding

    private val bookViewModel: BookViewModel by lazy {
        BookViewModelProvider(repository).create(BookViewModel::class.java)
    }
    private val viewModel: BookViewModel by viewModels<BookViewModel> {
        BookViewModelProvider(repository)
    }
    private val repository: Repository by lazy {
        RepositoryImpl(BookApi.getApi(), Dispatchers.IO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarBinding = BookToolbarBinding.bind(binding.root)
        println("ViewModel= ${bookViewModel.hashCode()}")
        setContentView(binding.root)
        initViews()
        navigateBookListFragment()
    }

    private fun initViews() {
        setSupportActionBar(toolbarBinding.bookToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.book_menu, menu)
        val actionMenu = menu?.findItem(R.id.book_search_menu)
        val searchView = actionMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        Log.d(TAG, "onQueryTextSubmit: ")
                        viewModel._inputStateFlow.value = query
                    }
                    searchView.isIconified = true
                    searchView.clearFocus()
                    actionMenu.collapseActionView()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel._inputStateFlow.update {
                        Log.d(TAG, "onQueryTextChange: $newText")
                        newText!!
                    }
                    return true
                    //bookViewModel._inputStateFlow.debounce(2000).single()
                    //return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }

}