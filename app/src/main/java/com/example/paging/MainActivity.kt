package com.example.paging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource.Factory
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging.databinding.ActivityMainBinding
import com.example.paging.databinding.MonItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var api: PokeAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setup()
    }

    private fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PokeAPI::class.java)
        val adapter = MainRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        createLiveData().observe(this, Observer { result -> adapter.submitList(result) })
    }

    private fun createLiveData(): LiveData<PagedList<Result>> {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .setPrefetchDistance(10)
            .build()

        return LivePagedListBuilder(object : Factory<String, Result>() {
            override fun create(): androidx.paging.DataSource<String, Result> {
                return DataSource()
            }
        }, config).build()
    }

    private val diffItem = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.name == newItem.name && oldItem.url == newItem.url
    }

    private inner class MainRecyclerViewAdapter :
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

    private inner class MainViewHolder(val monBinding: MonItemBinding) :
        RecyclerView.ViewHolder(monBinding.root) {
        fun setData(data: Result) {
            monBinding.monTextView.text = data.name
        }
    }

    private inner class DataSource : PageKeyedDataSource<String, Result>() {
        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<String, Result>
        ) {
            api?.run {
                listPokemons().enqueue(object : Callback<Response> {
                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        response.body()?.let {
                            callback.onResult(it.results, it.previous, it.next)
                            Log.d("PagingTest", "loadInitial ${it.results.size} ${it.next}")
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        callback.onResult(emptyList(), null, null)
                    }
                })
            }
        }

        override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Result>) {
            val queryPart = params.key.split("?")[1]
            val queries = queryPart.split("&")
            val map = mutableMapOf<String, String>()
            for(query in queries) {
                val parts = query.split("=")
                map[parts[0]] = parts[1]
            }

            try {
                api?.listPokemons(map["offset"]!!, map["limit"]!!)?.enqueue(object: Callback<Response> {

                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        response.body()?.let {
                            callback.onResult(it.results,  it.previous)
                            Log.d("PagingTest", "loadBefore ${it.results.size} ${it.previous}")
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        callback.onResult(emptyList(), null)
                    }
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Result>) {
            val queryPart = params.key.split("?")[1]
            val queries = queryPart.split("&")
            val map = mutableMapOf<String, String>()
            for(query in queries) {
                val parts = query.split("=")
                map[parts[0]] = parts[1]
            }

            try {
                api?.listPokemons(map["offset"]!!, map["limit"]!!)?.enqueue(object: Callback<Response> {

                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        response.body()?.let {
                            callback.onResult(it.results,  it.next)
                            Log.d("PagingTest", "loadAfter ${it.results.size} ${it.next}")
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        callback.onResult(emptyList(), null)
                    }
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}