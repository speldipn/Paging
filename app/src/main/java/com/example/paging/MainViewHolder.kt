package com.example.paging

import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.MonItemBinding

class MainViewHolder : RecyclerView.ViewHolder {

  private val viewModel: ViewModel = ViewModel()

  constructor(binding: MonItemBinding) : super(binding.root) {
    binding.viewModel = viewModel
  }

  fun setData(data: Result) {
    val tokens = if (data.url.endsWith("/")) {
      data.url.dropLast(1).split("/")
    } else {
      data.url.split("/")
    }
    val id = tokens.last()

    viewModel.name.postValue(data.name)
    viewModel.url.postValue(data.url)
    viewModel.imageUrl.postValue("https://pokeres.bastionbot.org/images/pokemon/$id.png")
  }

}
