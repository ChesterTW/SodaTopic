package com.taro.sodatopic.activity.gameActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.R
import com.taro.sodatopic.api.ApiService
import com.taro.sodatopic.databinding.ActivityGameBinding
import com.taro.sodatopic.databinding.RowGameBinding
import com.taro.sodatopic.databinding.RowGameRecommandBinding
import com.taro.sodatopic.model.CultureData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GameActivity : AppCompatActivity() {
    companion object{
        private val TAG = GameActivity::class.java.simpleName
        private const val URL = "https://themedata.culture.tw/opendata/object/"
    }

    private lateinit var binding: ActivityGameBinding
    private lateinit var games: List<CultureData.Data>


    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val gameService = retrofit.create(ApiService::class.java)

    private val mAdapter = GameAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycler()
        callApi(gameService)

    }

    @SuppressLint("CheckResult")
    private fun callApi(gameService: ApiService) {
        gameService.listAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "onCreate: Request Success.")
                    mAdapter.setData(it.data)
                }, {
                    Log.d(TAG, "onCreate: Request Error, Message: $it")
                }, {
                    Log.d(TAG, "onCreate: Request Completed.")
                }
            )
    }

    private fun recycler() {
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = GameAdapter()
    }



}




