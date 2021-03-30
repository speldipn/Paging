package com.example.paging

import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.MonItemBinding

class MainViewHolder(val binding: MonItemBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun setData(data: Result) {
    val tokens = if (data.url.endsWith("/")) {
      data.url.dropLast(1).split("/")
    } else {
      data.url.split("/")
    }
    val id = tokens.last()
    binding.imageView.setImageURI("https://pokeres.bastionbot.org/images/pokemon/$id.png")
    binding.monTextView.text = data.name
  }
}
