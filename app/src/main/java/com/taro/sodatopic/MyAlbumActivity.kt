package com.taro.sodatopic

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.databinding.ActivityMyAlbumBinding
import com.taro.sodatopic.databinding.CardAlbumBinding
import com.taro.sodatopic.databinding.CardAlbumNoDataBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MyAlbumActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyAlbumBinding
    private val TAG = MyAlbumActivity::class.java.simpleName
    private lateinit var Albums: List<AlbumData.Data>

    private var URL = "https://themedata.culture.tw/opendata/object/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMyAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val albumService = retrofit.create(AlbumService::class.java)

        albumService.listAlbums()
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

    private fun recycler() {
        var layoutManager = GridLayoutManager(this, 2)
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = AlbumAdapter()

        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if(position == AlbumAdapter().itemCount-1){
                    2
                }else{
                    1
                }
            }

        }


    }

    inner class AlbumAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var VIEW_TYPE_FIRST = 1
        private var VIEW_TYPE_LASTONE = 2

        override fun getItemViewType(position: Int): Int {
            /// 判斷是否為最後一個 ROW，要提供 No Data Card。
            return if(position == (itemCount-1)) VIEW_TYPE_LASTONE else VIEW_TYPE_FIRST
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                VIEW_TYPE_FIRST -> {
                    var view = LayoutInflater.from(this@MyAlbumActivity)
                        .inflate(R.layout.card_album, parent, false)
                    CardAlbumViewHolder(view)
                }
                VIEW_TYPE_LASTONE -> {
                    var view = LayoutInflater.from(this@MyAlbumActivity)
                        .inflate(R.layout.card_album_no_data, parent, false)
                    Log.d(TAG, "onCreateViewHolder: The last one")
                    CardAlbumNoDataViewHolder(view)
                }
                else -> throw IllegalArgumentException("Invalid viewType")
            }

        }

        override fun getItemCount(): Int {
            return Albums.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var data = Albums[position]
            when(holder){
                is CardAlbumViewHolder -> {
                    holder.bind(data)
                }
                is CardAlbumNoDataViewHolder -> {}
            }
        }

    }

    inner class CardAlbumViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding = CardAlbumBinding.bind(view)
        var titleText = binding.albumTitle
        var dateText = binding.albumDate
        var albumImage = binding.albumPreview
        var imgCount = binding.albumNumber

        fun bind(data: AlbumData.Data){
            titleText.text = data.name
            dateText.text = data.id.toString()

            Glide.with(this@MyAlbumActivity)
                .load(data.images.first())
                .into(albumImage)

            imgCount.text = data.images.size.toString()
        }

    }

    inner class CardAlbumNoDataViewHolder(view: View) : RecyclerView.ViewHolder(view){}
}

interface AlbumService {
    @GET("Point.json?origin=tour.cksmh.gov.tw&lang=zh-tw&branch=home")
    fun listAlbums() : Observable<AlbumData>
}

data class AlbumData(
    val `data`: List<Data>
){
    data class Data(
        val content: String,
        val id: Int,
        val images: List<String>,
        val link: String,
        val name: String
    )
}

