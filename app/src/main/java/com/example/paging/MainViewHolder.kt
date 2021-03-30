package com.example.paging

import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.MonItemBinding

class MainViewHolder(val monBinding: MonItemBinding) :
  RecyclerView.ViewHolder(monBinding.root) {
  fun setData(data: Result) {
    monBinding.monTextView.text = data.name
  }
}
