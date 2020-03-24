package com.example.myrxsample.error

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myrxsample.R
import com.example.myrxsample.utils.ext.appendLine
import com.example.myrxsample.entity.BaseEntity
import com.example.myrxsample.entity.UserInfo
import com.example.myrxsample.processor.GlobalErrorProcessor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_toast.*
import org.json.JSONException

class ToastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast)

        clickIt.setOnClickListener {
            Observable.error<BaseEntity<UserInfo>>(JSONException("JSONException"))
                .compose(GlobalErrorProcessor.processGlobalError<BaseEntity<UserInfo>>(this)) // 彈一個toast
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data!! }
                .subscribe({ userInfo ->
                    logs.appendLine("request successful，user info：$userInfo")
                }, { error ->
                    logs.appendLine("request fail，error msg：$error")
                })
        }
    }
}
