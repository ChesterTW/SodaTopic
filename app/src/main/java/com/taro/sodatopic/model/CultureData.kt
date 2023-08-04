package com.taro.sodatopic.model

data class CultureData(
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