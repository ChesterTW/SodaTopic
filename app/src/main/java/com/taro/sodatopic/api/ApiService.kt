package com.taro.sodatopic.api

import com.taro.sodatopic.model.CultureData
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
    @GET("Point.json?origin=tour.cksmh.gov.tw&lang=zh-tw&branch=home")
    fun listAlbums() : Observable<CultureData>
}