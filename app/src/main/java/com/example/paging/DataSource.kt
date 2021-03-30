package com.example.paging

import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException

class DataSource(private val pokeApi: PokeAPI) : PageKeyedDataSource<String, Result>() {

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, Result>
  ) {
    try {
      pokeApi.listPokemons("0", "20").enqueue(object : Callback<Response> {
        override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
          response.body()?.let {
            callback.onResult(it.results, it.previous, it.next)
          }
        }

        override fun onFailure(call: Call<Response>, t: Throwable) {
          callback.onResult(emptyList(), null, null)
        }
      })
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Result>) {
    val offset = parseQuery(params.key)["offset"]!!
    val limit = parseQuery(params.key)["limit"]!!

    try {
      pokeApi.listPokemons(offset, limit).enqueue(object : Callback<Response> {
        override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
          response.body()?.let {
            callback.onResult(it.results, it.previous)
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

  private fun parseQuery(url: String): MutableMap<String, String> {
    val queryPart = url.split("?")[1]
    val queries = queryPart.split("&")
    val map = mutableMapOf<String, String>()
    for (query in queries) {
      val parts = query.split("=")
      map[parts[0]] = parts[1]
    }
    return map
  }

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Result>) {
    val offset = parseQuery(params.key)["offset"]!!
    val limit = parseQuery(params.key)["limit"]!!

    try {
      pokeApi.listPokemons(offset, limit).enqueue(object : Callback<Response> {
        override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
          response.body()?.let {
            callback.onResult(it.results, it.next)
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
