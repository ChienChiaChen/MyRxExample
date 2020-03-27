package com.example.myrxsample.entity

import java.util.ArrayList

data class NewsResultEntity(
    var createdAt: String = "",
    var desc: String = "",
    var images: List<String> = ArrayList(),
    var publishedAt: String = "",
    var source: String = "",
    var type: String = "",
    var url: String = "",
    var used: Boolean = false,
    var who: String = ""
)
