package com.taro.sodatopic

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Observable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.databinding.ActivityGameBinding
import com.taro.sodatopic.databinding.RowGameBinding
import com.taro.sodatopic.databinding.RowGameRecommandBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val TAG = GameActivity::class.java.simpleName
    private lateinit var games: List<GameData.Data>

    private var URL = "https://themedata.culture.tw/opendata/object/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameService = retrofit.create(GameService::class.java)

        gameService.listGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    games = it.data
                    Log.d(TAG, "onCreate: Request Success.")
                    recycler()
                },{
                    Log.d(TAG, "onCreate: Request Error, Message: $it")
                },{
                    Log.d(TAG, "onCreate: Request Completed.")
                }
            )





    }

    private fun recycler() {
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = GameAdapter()
    }

    inner class GameAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var VIEW_TYPE_FIRST = 1
        private var VIEW_TYPE_SECOND = 2

        override fun getItemViewType(position: Int): Int {
            /// 判斷是否為第一個 ROW，要提供 Recommend Game Row。
            return if(position == 0) VIEW_TYPE_FIRST else VIEW_TYPE_SECOND
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                VIEW_TYPE_FIRST -> {
                    var view = LayoutInflater.from(this@GameActivity)
                        .inflate(R.layout.row_game_recommand,parent,false)
                    RecommendViewHolder(view)
                }
                VIEW_TYPE_SECOND -> {
                    var view = LayoutInflater.from(this@GameActivity)
                        .inflate(R.layout.row_game,parent,false)
                    NormalViewHolder(view)
                }
                else -> throw IllegalArgumentException("Invalid viewType")
            }

        }

        override fun getItemCount(): Int {
            return games.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var data = games[position]
            when(holder){
                is RecommendViewHolder -> {
                    holder.bind(data)
                }
                is NormalViewHolder -> {
                    holder.bind(data)
                }
            }

        }

    }

    inner class RecommendViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding = RowGameRecommandBinding.bind(view)
        var titleText = binding.gameTitle
        var describeText = binding.gameDescribe
        var memberText = binding.gameMember
        var button = binding.button
        var gameImage = binding.gameImage

        fun bind(data: GameData.Data){
            titleText.text = data.name
            describeText.text = data.content
            memberText.text = data.id.toString()
            button.setOnClickListener(View.OnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.link)))
            })
            Glide.with(this@GameActivity)
                .load(data.images.first())
                .into(gameImage)

        }
    }

    inner class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding = RowGameBinding.bind(view)
        var titleText = binding.gameTitle
        var describeText = binding.gameDescribe
        var memberText = binding.gameMember
        var button = binding.button
        var gameImage = binding.gameImage

        fun bind(data: GameData.Data){
            titleText.text = data.name
            describeText.text = data.content
            memberText.text = data.id.toString()
            button.setOnClickListener(View.OnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.link)))
            })
            Glide.with(this@GameActivity)
                .load(data.images.first())
                .into(gameImage)

        }
    }


}

interface GameService {
    @GET("Point.json?origin=tour.cksmh.gov.tw&lang=zh-tw&branch=home")
    fun listGames() : io.reactivex.Observable<GameData>
}

data class GameData(
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



