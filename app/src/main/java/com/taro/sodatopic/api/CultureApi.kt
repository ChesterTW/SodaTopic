package com.taro.sodatopic.api

import com.taro.sodatopic.model.CultureData
import io.reactivex.Observable

object CultureApi {
    fun getCultureData(): Observable<CultureData> {
        return ApiServiceManger.callApi()
    }
}