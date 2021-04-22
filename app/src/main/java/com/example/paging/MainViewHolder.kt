package com.example.paging

import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.MonItemBinding

class MainViewHolder(binding: MonItemBinding) : RecyclerView.ViewHolder(binding.root) {

  val name = ObservableField<String>()
  val url = ObservableField<String>()
  val imageUrl = ObservableField<String>()

  init {
    binding.data = this
  }

  fun setData(data: Result) {
    val tokens = if (data.url.endsWith("/")) {
      data.url.dropLast(1).split("/")
    } else {
      data.url.split("/")
    }
    val id = tokens.last()

    name.set(data.name)
    url.set(data.url)
    imageUrl.set("https://pokeres.bastionbot.org/images/pokemon/$id.png")

//    basic data binding
//    binding.imageView.setImageURI("https://pokeres.bastionbot.org/images/pokemon/$id.png")
//    binding.monTextView.text = data.name
//    binding.urlTextView.text = data.url
  }

}
