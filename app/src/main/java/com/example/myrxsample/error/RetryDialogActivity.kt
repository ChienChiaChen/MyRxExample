package com.example.myrxsample.error

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myrxsample.R
import com.example.myrxsample.entity.BaseEntity
import com.example.myrxsample.entity.UserInfo
import com.example.myrxsample.processor.GlobalErrorProcessor
import com.example.myrxsample.utils.ext.appendLine
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_retry_dialog.*
import java.net.ConnectException

class RetryDialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retry_dialog)
        clickIt.setOnClickListener {
            Observable.error<BaseEntity<UserInfo>>(ConnectException())
                .compose(GlobalErrorProcessor.processGlobalError<BaseEntity<UserInfo>>(this)) // 弹出dialog
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data!! }
                .subscribe({ userInfo ->
                    logs.appendLine("接口请求成功，用户信息：$userInfo")
                }, { error ->
                    logs.appendLine("接口请求失败，异常信息：$error")
                })
        }
    }
}
