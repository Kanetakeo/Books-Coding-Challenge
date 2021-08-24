package com.example.booksearch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.booksearch.R
import com.example.booksearch.databinding.BookDetailLayoutBinding
import com.example.booksearch.model.AppState
import com.example.booksearch.model.BookResponse
import com.example.booksearch.model.Repository
import com.example.booksearch.model.RepositoryImpl
import com.example.booksearch.model.remote.BookApi
import com.example.booksearch.util.hideShowProgressBar
import com.example.booksearch.util.showError
import com.example.booksearch.viewmodel.BookViewModel
import com.example.booksearch.viewmodel.provider.BookViewModelProvider
import kotlinx.coroutines.Dispatchers

class BookDetailFragment: Fragment() {
    companion object{
        const val EXTRA_BOOK_POSITION: String = "BookDetailFragment"

        fun bookDetail(itemPosition: Int): BookDetailFragment{
            return BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_BOOK_POSITION, itemPosition)
                }
            }
        }
    }

    private var _binding: BookDetailLayoutBinding? = null
    private val binding: BookDetailLayoutBinding get() =_binding!!

    private val viewModel by activityViewModels<BookViewModel>{
        BookViewModelProvider(repository)
    }

    private val repository: Repository by lazy {
        RepositoryImpl(BookApi.getApi(), Dispatchers.IO)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = BookDetailLayoutBinding.inflate(inflater, container, false)
        viewModel.book.observe(viewLifecycleOwner){
            when (it){
                is AppState.Response -> updateView(it.data)
                is AppState.Error -> activity?.showError(it.error)
                is AppState.Loading -> activity?.hideShowProgressBar(it.isLoading)
            }
        }
        return binding.root
    }

    private fun updateView(data: BookResponse) {
        arguments?.getInt(EXTRA_BOOK_POSITION)?.let {
            data.items[it].run {
                binding.bookDetailAuthors.text = context?.getString(R.string.book_detail_authors,
                    volumeInfo.authors.toString())
                binding.bookDetailDescription.text = context?.getString(R.string.book_detail_description,
                    volumeInfo.description)
                binding.bookDetailPublisher.text = context?.getString(R.string.book_detail_publisher,
                    volumeInfo.publisher)
                Glide.with(binding.root).load(volumeInfo.imageLinks.thumbnail).into(binding.bookDetailImage)
            }
        }
    }
}