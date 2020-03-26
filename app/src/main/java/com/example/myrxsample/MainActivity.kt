package com.example.myrxsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myrxsample.activities.SearchActivity
import com.example.myrxsample.error.RetryDialogActivity
import com.example.myrxsample.error.ToastActivity
import com.example.myrxsample.error.TokenExpiredActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToErrorToastPage(view: View) {
        startActivity(Intent(this, ToastActivity::class.java))
    }

    fun goToRetryDialogPage(view: View) {
        startActivity(Intent(this, RetryDialogActivity::class.java))
    }

    fun goToUpdatingTokenPage(view: View) {
        startActivity(Intent(this, TokenExpiredActivity::class.java))
    }

    fun goSearchPage(view: View) {
        startActivity(Intent(this, SearchActivity::class.java))
    }
}
