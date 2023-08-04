package com.taro.sodatopic.activity.myAlbumActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.taro.sodatopic.api.ApiService
import com.taro.sodatopic.databinding.ActivityMyAlbumBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MyAlbumActivity : AppCompatActivity() {
    companion object{
        private val TAG = MyAlbumActivity::class.java.simpleName
        private const val URL = "https://themedata.culture.tw/opendata/object/"
    }

    lateinit var binding: ActivityMyAlbumBinding

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    private val mAdapter = MyAlbumAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMyAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycler()
        callApi()

    }
    private fun recycler() {
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if(position == MyAlbumAdapter().itemCount-1){
                        2
                    }else{
                        1
                    }
                }
            } }
        binding.recycler.adapter = mAdapter

    }
    @SuppressLint("CheckResult")
    private fun callApi(){
        apiService.listAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "onCreate: Request Success.")
                mAdapter.setData(it.data)
            },{
                Log.d(TAG, "onCreate: Request Failed, $it")
            },{
                Log.d(TAG, "onCreate: Request Completed.")
            })
    }

}



