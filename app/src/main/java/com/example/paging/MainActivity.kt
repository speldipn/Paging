package com.example.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource.Factory
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.paging.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val adapter by lazy { MainAdapter() }
  private val httpClient by lazy {
    OkHttpClient.Builder().addInterceptor(
      HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    ).build()
  }
  private val pokeApi by lazy {
    Retrofit.Builder()
      .client(httpClient)
      .baseUrl("https://pokeapi.co/api/v2/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(PokeAPI::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    setup()
  }

  private fun setup() {
    setupAdapter()
    createLiveData().observe(this, Observer { result ->
      adapter.submitList(result)
    })
  }

  private fun setupAdapter() {
    binding.recyclerView.adapter = adapter
  }

  private fun createLiveData(): LiveData<PagedList<Result>> {
    // Paging configuration
    val config = PagedList.Config.Builder()
      .setInitialLoadSizeHint(20) //  first size
      .setPageSize(20) // count page size
      .setPrefetchDistance(10) // pageSize - sePrefetchDistance = 10(prefetch interval)
      .build()

    // Create data source
    return LivePagedListBuilder(object : Factory<String, Result>() {
      override fun create(): androidx.paging.DataSource<String, Result> {
        return DataSource(pokeApi)
      }
    }, config).build()
  }

}