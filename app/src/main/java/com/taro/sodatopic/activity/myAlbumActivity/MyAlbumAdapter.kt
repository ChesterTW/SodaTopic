package com.taro.sodatopic.activity.myAlbumActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taro.sodatopic.R
import com.taro.sodatopic.databinding.CardAlbumBinding
import com.taro.sodatopic.model.CultureData

class MyAlbumAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        private const val VIEW_TYPE_FIRST = 1
        private const val VIEW_TYPE_LAST_ONE = 2
    }

    override fun getItemViewType(position: Int): Int {
        /// 判斷是否為最後一個 ROW，要提供 No Data Card。
        return if(position == (itemCount-1)) VIEW_TYPE_LAST_ONE else VIEW_TYPE_FIRST
    }

    private var mAlbums: List<CultureData.Data> = listOf()
    fun setData(albums: List<CultureData.Data>){
        mAlbums = albums
        notifyItemRangeChanged(0,albums.size+1)
//        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FIRST -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_album, parent, false)
                CardAlbumViewHolder(view)
            }
            VIEW_TYPE_LAST_ONE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_album_no_data, parent, false)
                CardAlbumNoDataViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid viewType")
        }

    }

    override fun getItemCount(): Int {
        return mAlbums.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = mAlbums[position]
        when(holder){
            is CardAlbumViewHolder -> {
                holder.bind(data)
            }
            is CardAlbumNoDataViewHolder -> {}
        }
    }

    inner class CardAlbumViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = CardAlbumBinding.bind(view)

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