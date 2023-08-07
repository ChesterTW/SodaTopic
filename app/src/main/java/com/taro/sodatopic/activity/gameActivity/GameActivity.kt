package com.taro.sodatopic.activity.gameActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
    }

    private lateinit var binding: ActivityGameBinding

    private val viewModel: GameViewModel by lazy{
        ViewModelProvider(this)[GameViewModel::class.java]
    }

//    private val mAdapter = GameAdapter(viewModel)
    private val mAdapter by lazy{
        GameAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initModel()

    }

    private fun initModel() {
        viewModel.getCultureData()
        viewModel.cultureLiveData.observe(this){
            Log.d("DEBUG_ADAPTER", "${it.data}")
            mAdapter.setData(it.data)
        }
        Log.d("DEBUG_ADAPTER", "initModel Complete.")
    }

    private fun initView() {
        with(binding){
            recycler.setHasFixedSize(true)
            recycler.layoutManager = LinearLayoutManager(this@GameActivity)
            recycler.adapter = mAdapter
            Log.d("DEBUG_ADAPTER", "initView Complete.")
        }
    }

}




