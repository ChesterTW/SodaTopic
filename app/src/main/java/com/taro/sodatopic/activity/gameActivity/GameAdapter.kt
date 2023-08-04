package com.taro.sodatopic.activity.gameActivity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.R
import com.taro.sodatopic.databinding.RowGameBinding
import com.taro.sodatopic.databinding.RowGameRecommandBinding
import com.taro.sodatopic.model.CultureData

class GameAdapter(var games: List<CultureData.Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var VIEW_TYPE_FIRST = 1
    private var VIEW_TYPE_SECOND = 2

    override fun getItemViewType(position: Int): Int {
        /// 判斷是否為第一個 ROW，要提供 Recommend Game Row。
        return if(position == 0) VIEW_TYPE_FIRST else VIEW_TYPE_SECOND
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FIRST -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_game_recommand,parent,false)
                RecommendViewHolder(view)
            }
            VIEW_TYPE_SECOND -> {
                val view = LayoutInflater.from(parent.context)
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
        val data = games[position]
        when(holder){
            is RecommendViewHolder -> {
                holder.bind(data)
            }
            is NormalViewHolder -> {
                holder.bind(data)
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

        fun bind(data: CultureData.Data){
            titleText.text = data.name
            describeText.text = data.content
            memberText.text = data.id.toString()
            button.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                startActivity(itemView.context, intent, null)

            })
            Glide.with(itemView.context)
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

        fun bind(data: CultureData.Data){
            titleText.text = data.name
            describeText.text = data.content
            memberText.text = data.id.toString()
            button.setOnClickListener(View.OnClickListener {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.link)))
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                startActivity(itemView.context, intent, null)
            })
            Glide.with(itemView.context)
                .load(data.images.first())
                .into(gameImage)

        }
    }

}