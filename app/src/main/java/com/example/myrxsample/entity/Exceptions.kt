package com.example.myrxsample.entity

sealed class Errors : Exception() {

    object ConnectFailedException : Errors()

    data class AuthorizationError(val timeStamp: Long) : Errors()
}