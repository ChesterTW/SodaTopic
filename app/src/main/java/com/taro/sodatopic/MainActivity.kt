package com.taro.sodatopic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taro.sodatopic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = "轻松赚轻松玩爽爽大家一起轻松爽赚钱拿紅包"
    }
}