package com.example.paging

import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.MonItemBinding

class MainViewHolder(private val binding: MonItemBinding) : RecyclerView.ViewHolder(binding.root) {

//  private val viewModel: ViewModel = ViewModel()

//  init {
//    binding.viewModel = viewModel
//  }

  fun setData(data: Result) {
    val tokens = if (data.url.endsWith("/")) {
      data.url.dropLast(1).split("/")
    } else {
      data.url.split("/")
    }
    val id = tokens.last()

    binding.monTextView.text = data.name
    binding.urlTextView.text = data.url
    binding.imageView.setImageURI("https://pokeres.bastionbot.org/images/pokemon/$id.png")
  }

}
