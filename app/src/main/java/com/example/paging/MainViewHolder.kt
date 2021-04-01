package com.example.paging

import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.MonItemBinding

class MainViewHolder: RecyclerView.ViewHolder {

  private val viewModel: ViewModel = ViewModel()

  constructor(binding: MonItemBinding): super(binding.root){
    binding.viewModel = viewModel
  }

  fun setData(data: Result) {
    val tokens = if (data.url.endsWith("/")) {
      data.url.dropLast(1).split("/")
    } else {
      data.url.split("/")
    }
    val id = tokens.last()

    viewModel.name.set(data.name)
    viewModel.url.set(data.url)
    viewModel.imageUrl.set("https://pokeres.bastionbot.org/images/pokemon/$id.png")
  }
}
