package com.example.myrxsample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.example.myrxsample.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_rx_login.*


class RxLoginActivity : AppCompatActivity() {

    private val mNamePublishSubject: PublishSubject<String> = PublishSubject.create()
    private val mPasswordPublishSubject: PublishSubject<String> = PublishSubject.create()
    private val compositeDisposable: CompositeDisposable by lazy(LazyThreadSafetyMode.NONE) { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_login)

        et_name.doAfterTextChanged { mNamePublishSubject.onNext(it.toString()) }
        et_password.doAfterTextChanged { mPasswordPublishSubject.onNext(it.toString()) }

        compositeDisposable.add(
            Observable.combineLatest(
                mNamePublishSubject,
                mPasswordPublishSubject,
                validateNamePwd()
            ).subscribe { value ->
                bt_login.text = if (value) "Login" else "Invalid"
            }
        )

    }

    private fun validateNamePwd(): BiFunction<String, String, Boolean> {
        return BiFunction { name, pwd ->
            !name.isNullOrBlank() && !pwd.isNullOrBlank()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}