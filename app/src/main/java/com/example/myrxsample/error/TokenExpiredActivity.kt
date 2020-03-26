package com.example.myrxsample.error

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myrxsample.R
import com.example.myrxsample.api.FakeDataSource
import com.example.myrxsample.entity.BaseEntity
import com.example.myrxsample.entity.UserInfo
import com.example.myrxsample.processor.GlobalErrorProcessor
import com.example.myrxsample.utils.ext.appendLine
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_token_expired.*
import java.util.concurrent.TimeUnit

class TokenExpiredActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token_expired)
        clickIt.setOnClickListener {
            Observable.fromCallable(FakeDataSource::queryUserInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { entity ->
                    when (GlobalErrorProcessor.STATUS_OK == entity.statusCode) {
                        true -> { logs.appendLine("Ok") }
                        false -> { logs.appendLine("Fail~~") }
                    }
                }
                .observeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .compose(GlobalErrorProcessor.processGlobalError<BaseEntity<UserInfo>>(this))
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data!! }
                .subscribe(
                    { info -> logs.appendLine("Ok, user info::$info") },
                    { error -> logs.appendLine("Fail~~ error::$error")
                })

        }
    }
}
