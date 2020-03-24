package com.example.myrxsample.processor

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.myrxsample.core.GlobalErrorTransformer
import com.example.myrxsample.core.retry.RetryConfig
import com.example.myrxsample.entity.BaseEntity
import com.example.myrxsample.entity.Errors
import com.example.myrxsample.utils.ui.RxDialog
import io.reactivex.Observable
import org.json.JSONException
import java.net.ConnectException

object GlobalErrorProcessor {

    const val STATUS_OK = 200
    const val STATUS_UNAUTHORIZED = 401

    fun <T : BaseEntity<*>> processGlobalError(
        fragmentActivity: FragmentActivity
    ): GlobalErrorTransformer<T> =
        GlobalErrorTransformer(
            onNextInterceptor = {
                when (it.statusCode) {
                    STATUS_UNAUTHORIZED -> Observable.error(Errors.AuthorizationError(timeStamp = System.currentTimeMillis()))
                    else -> Observable.just(it)
                }
            },
            onErrorResumeNext = { error ->
                when (error) {
                    is ConnectException -> Observable.error<T>(Errors.ConnectFailedException)
                    is Errors.AuthorizationError -> Observable.error<T>(error) // 这个错误会在onErrorRetrySupplier()中处理
                    else -> Observable.error<T>(error)
                }
            },
            onErrorRetrySupplier = { retrySupplierError ->
                when (retrySupplierError) {
                    Errors.ConnectFailedException -> RetryConfig.simpleInstance {
                        RxDialog.showErrorDialog(fragmentActivity, "ConnectException")
                    }
                    else -> RetryConfig.none()// No retry
                }
            },
            onErrorConsumer = { error ->
                when (error) {
                    is JSONException -> {
                        Toast.makeText(fragmentActivity, "$error", Toast.LENGTH_SHORT).show()
                        Log.w("rx stream Exception", "Json解析異常:${error.message}")
                    }
                    else -> Log.w("rx stream Exception", "其它異常:${error.message}")
                }
            }

        )
}