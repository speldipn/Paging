package com.example.paging

import androidx.lifecycle.MutableLiveData

class ViewModel {
  var name = MutableLiveData<String>()
  var url = MutableLiveData<String>()
  var imageUrl = MutableLiveData<String>()
}