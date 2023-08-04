package com.taro.sodatopic.activity.myAlbumActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.R
import com.taro.sodatopic.databinding.CardAlbumBinding
import com.taro.sodatopic.model.CultureData

class MyAlbumAdapter(private val Albums: List<CultureData.Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var VIEW_TYPE_FIRST = 1
    private var VIEW_TYPE_LASTONE = 2

    override fun getItemViewType(position: Int): Int {
        /// 判斷是否為最後一個 ROW，要提供 No Data Card。
        return if(position == (itemCount-1)) VIEW_TYPE_LASTONE else VIEW_TYPE_FIRST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FIRST -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_album, parent, false)
                CardAlbumViewHolder(view)
            }
            VIEW_TYPE_LASTONE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_album_no_data, parent, false)
                CardAlbumNoDataViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid viewType")
        }

    }

    override fun getItemCount(): Int {
        return Albums.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = Albums[position]
        when(holder){
            is CardAlbumViewHolder -> {
                holder.bind(data)
            }
            is CardAlbumNoDataViewHolder -> {}
        }
    }

    inner class CardAlbumViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var binding = CardAlbumBinding.bind(view)
        /*        var albumImage = binding.albumPreview
                var titleText = binding.albumTitle
                var dateText = binding.albumDate
                var imgCount = binding.albumPhoCount*/

        fun bind(data: CultureData.Data){

            with(binding){
                binding.albumDate.text = data.name
                binding.albumDate.text = data.id.toString()
                binding.albumPhoCount.text = data.images.size.toString()
                Glide.with(itemView.context)
                    .load(data.images.first())
                    .into(binding.albumPreview)
            }

        }

    }

    inner class CardAlbumNoDataViewHolder(view: View) : RecyclerView.ViewHolder(view){}

}