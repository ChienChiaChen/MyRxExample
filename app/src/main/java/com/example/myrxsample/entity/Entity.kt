package com.example.myrxsample.entity

data class BaseEntity<out T>(
        val statusCode: Int,
        val message: String,
        val data: T?
)

data class UserInfo(
        val username: String,
        val age: Int
)