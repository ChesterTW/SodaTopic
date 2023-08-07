package com.taro.sodatopic.activity.gameActivity

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taro.sodatopic.api.CultureApi
import com.taro.sodatopic.model.CultureData
import kotlin.math.log

class GameViewModel(): ViewModel(){
    val cultureLiveData = MutableLiveData<CultureData>()

    @SuppressLint("CheckResult")
    fun getCultureData(){
        CultureApi.getCultureData().subscribe({
            //cultureLiveData.value = it
            cultureLiveData.postValue(it)
            Log.d("DEBUG_GAME", "SUCCESS")
        },{
            Log.e("DEBUG_GAME","get culture data error ${it.message}")
        })
    }
}