package com.example.booksearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.booksearch.R
import com.example.booksearch.databinding.BookItemLayoutBinding
import com.example.booksearch.model.BookItem

class BookAdapter(private val callback: (Int) -> Unit) :
    ListAdapter<BookItem, BookAdapter.BookViewHolder>(DiffCallBack) {

    class BookViewHolder(
        private val binding: BookItemLayoutBinding,
        private val callback: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(bookItem: BookItem) {
            bookItem.volumeInfo.imageLinks?.thumbnail?.let {
                Glide.with(binding.root.context)
                    .load(it)
                    .into(binding.bookImage)
            }
            binding.bookDetails.text = binding.root.context.getString(
                R.string.book_detail,
                bookItem.volumeInfo.title,
                bookItem.volumeInfo.authors?.toString()
            )
            binding.root.setOnClickListener { callback(adapterPosition) }
        }
    }

    object DiffCallBack : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            BookItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}