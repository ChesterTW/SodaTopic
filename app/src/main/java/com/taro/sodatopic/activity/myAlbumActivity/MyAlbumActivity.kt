package com.taro.sodatopic.activity.myAlbumActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.R
import com.taro.sodatopic.api.ApiService
import com.taro.sodatopic.databinding.ActivityMyAlbumBinding
import com.taro.sodatopic.databinding.CardAlbumBinding
import com.taro.sodatopic.model.CultureData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MyAlbumActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyAlbumBinding
    private val TAG = MyAlbumActivity::class.java.simpleName
    private lateinit var Albums: List<CultureData.Data>
    private var URL = "https://themedata.culture.tw/opendata/object/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private fun recycler() {
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if(position == MyAlbumAdapter(Albums).itemCount-1){
                    2
                }else{
                    1
                }
            }
        } }
        binding.recycler.adapter = MyAlbumAdapter(Albums)

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMyAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = retrofit.create(ApiService::class.java)

        apiService.listAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Albums = it.data
                Log.d(TAG, "onCreate: Request Success.")
                recycler()
            },{
                Log.d(TAG, "onCreate: Request Failed, $it")
            },{
                Log.d(TAG, "onCreate: Request Completed.")
            })

    }

}



