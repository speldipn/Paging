package com.example.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.paging.databinding.MonItemBinding

private val diffItem = object : DiffUtil.ItemCallback<Result>() {
  override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
    oldItem.name == newItem.name

  override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
    oldItem.name == newItem.name && oldItem.url == newItem.url
}

class MainAdapter :
  PagedListAdapter<Result, MainViewHolder>(diffItem) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
    val binding: MonItemBinding = DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.mon_item,
      parent,
      false
    )
    return MainViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
    getItem(position)?.let { holder.setData(it) }
  }
}