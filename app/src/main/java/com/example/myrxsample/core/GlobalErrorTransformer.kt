package com.example.myrxsample.core


import com.example.myrxsample.core.retry.ObservableRetryDelay
import com.example.myrxsample.core.retry.RetryConfig
import io.reactivex.*

typealias OnNextInterceptor<T> = (T) -> Observable<T>
typealias OnErrorResumeNext<T> = (Throwable) -> Observable<T>
typealias OnErrorRetrySupplier = (Throwable) -> RetryConfig
typealias OnErrorConsumer = (Throwable) -> Unit

class GlobalErrorTransformer<T> constructor(
    private val onNextInterceptor: OnNextInterceptor<T> = { Observable.just(it) },
    private val onErrorResumeNext: OnErrorResumeNext<T> = { Observable.error(it) },
    private val onErrorRetrySupplier: OnErrorRetrySupplier = { RetryConfig.none() },
    private val onErrorConsumer: OnErrorConsumer = {}
) : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
            .flatMap { onNextInterceptor(it) }
            .onErrorResumeNext { throwable: Throwable ->
                onErrorResumeNext(throwable)
            }
            .retryWhen(ObservableRetryDelay(onErrorRetrySupplier))
            .doOnError(onErrorConsumer)

    }
}