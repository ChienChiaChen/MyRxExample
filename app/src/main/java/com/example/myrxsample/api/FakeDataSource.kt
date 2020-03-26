package com.example.myrxsample.api

import android.util.Log
import com.example.myrxsample.entity.BaseEntity
import com.example.myrxsample.entity.UserInfo
import com.example.myrxsample.processor.GlobalErrorProcessor
import com.example.myrxsample.processor.tokens.AuthorizationErrorProcessor

object FakeDataSource {


    fun queryUserInfo(): BaseEntity<UserInfo> {
        val currentTime = System.currentTimeMillis()
        val lastTokenRefreshTime = AuthorizationErrorProcessor.mLastRefreshTokenTimeStamp
        Log.w("Jason", "lastTokenRefreshTime::$lastTokenRefreshTime")
        return when (lastTokenRefreshTime != 0L && currentTime - lastTokenRefreshTime <= 15000) { // 1.5 秒內就算成功
            false -> BaseEntity(
                    statusCode = GlobalErrorProcessor.STATUS_UNAUTHORIZED,
                    message = "unauthorized",
                    data = null
            )
            true -> BaseEntity(
                    statusCode = GlobalErrorProcessor.STATUS_OK,
                    message = "success",
                    data = UserInfo("qingmei2", 26)
            )
        }
    }
}