package com.example.booksearch.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.booksearch.databinding.BookListLayoutBinding
import com.example.booksearch.model.AppState
import com.example.booksearch.model.Repository
import com.example.booksearch.model.RepositoryImpl
import com.example.booksearch.model.remote.BookApi
import com.example.booksearch.util.hideShowProgressBar
import com.example.booksearch.util.openDetailFragment
import com.example.booksearch.util.showError
import com.example.booksearch.view.adapter.BookAdapter
import com.example.booksearch.viewmodel.BookViewModel
import com.example.booksearch.viewmodel.provider.BookViewModelProvider
import kotlinx.coroutines.Dispatchers

private const val TAG = "BookListFragment"

class BookListFragment: Fragment() {

    private val viewModel by activityViewModels<BookViewModel>{
        BookViewModelProvider(repository)
    }

    private val repository: Repository by lazy {
        RepositoryImpl(BookApi.getApi(), Dispatchers.IO)
    }
    private var _binding: BookListLayoutBinding? = null
    private val binding: BookListLayoutBinding get() = _binding!!
    private val adapter: BookAdapter by lazy {
        BookAdapter(::openDetailFragment)
    }

    private fun openDetailFragment(position: Int) {
        activity?.openDetailFragment(position)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = BookListLayoutBinding.inflate(inflater,
        container,
        false)
        initViews()
        initObservable()
        return binding.root
    }

    private fun initObservable() {
        viewModel.book.observe(viewLifecycleOwner){
            when (it){
                is AppState.Response -> adapter.submitList(it.data.items)
                is AppState.Error -> activity?.showError(it.error)
                is AppState.Loading -> activity?.hideShowProgressBar(it.isLoading)
            }
        }
    }

    private fun initViews() {
        binding.bookList.layoutManager = GridLayoutManager(context,2)
        binding.bookList.adapter = adapter
    }
}