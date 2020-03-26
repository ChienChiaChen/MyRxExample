package com.example.myrxsample.error.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myrxsample.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

@SuppressWarnings("CheckResult")
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mBtnSuccess.setOnClickListener { loginAsync(true) }
        mBtnFailed.setOnClickListener { loginAsync(false) }
    }

    private fun loginAsync(success: Boolean) {
        when (success) {
            // 模擬消耗2秒，作為使用者一系列登入的過程
            true ->
                Observable.just(true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { mProgressBar.visibility = View.VISIBLE } // show progressBar
                    .observeOn(Schedulers.io())
                    .delay(2, TimeUnit.SECONDS)   // 延遲兩秒延迟两秒
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mProgressBar.visibility = View.GONE     // hide progressBar
                        onLoginResult(true)
                    }
            // 登錄失敗不需要模擬Time-consumed, 因為使用者可能按下返回鍵來關閉頁面
            false -> onLoginResult(false)
        }
    }

    private fun onLoginResult(success: Boolean) {
        val intent = Intent().putExtra(EXTRA_SUCCESS, success)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val EXTRA_SUCCESS = "EXTRA_SUCCESS"
    }
}
