package com.example.myrxsample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrxsample.R
import com.example.myrxsample.adapters.NewsAdapter
import com.example.myrxsample.entity.NewsResultEntity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cache.*
import java.util.ArrayList

class CacheActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CacheActivity"
    }

    private val mNewsResultEntities = ArrayList<NewsResultEntity>()
    private val mCompositeDisposable: CompositeDisposable by lazy(LazyThreadSafetyMode.NONE) {
        CompositeDisposable()
    }

    private val mNewsAdapter: NewsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        NewsAdapter(mNewsResultEntities)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache)

        rv_news.apply {

        }
        rv_news.layoutManager = LinearLayoutManager(this)
        rv_news.adapter = mNewsAdapter

        bt_publish_refresh.setOnClickListener {

            mCompositeDisposable.add(
                getNetworkArticle(1500)
                    .subscribeOn(Schedulers.io())
                    .publish { network ->
                        return@publish Observable.merge(
                            network,
                            getCacheArticle(500).subscribeOn(Schedulers.io()).takeUntil(network)
                        )
                    }.observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mNewsResultEntities.clear()
                        mNewsResultEntities.addAll(it)
                        mNewsAdapter.notifyDataSetChanged()
                    }, {
                        Log.i(TAG, "Load error: e=${it.message}")
                    }, {
                        Log.i(TAG, "Loaded~")
                    })
            )
        }
    }

    private fun getNetworkArticle(simulateTime: Long): Observable<List<NewsResultEntity>> {
        return Observable.create(ObservableOnSubscribe<List<NewsResultEntity>> { emitter ->
            try {
                Log.i(TAG, "Start network data")
                Thread.sleep(simulateTime)
                val results = arrayListOf<NewsResultEntity>()
                for (i in 0..9) {
                    val entity = NewsResultEntity().apply {
                        type = "Network"
                        desc = "Desc=$i"
                    }
                    results.add(entity)
                }

                emitter.onNext(results)
                emitter.onComplete()

                Log.i(TAG, "End network data")
            } catch (e: Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        })
            .onErrorResumeNext(Function<Throwable, ObservableSource<out List<NewsResultEntity>>> { throwable ->
                Log.d(TAG, "網路請求發生錯誤 Throwable=$throwable")
                Observable.never()
            })
    }

    private fun getCacheArticle(simulateTime: Long): Observable<List<NewsResultEntity>> {
        return Observable.create { observableEmitter ->
            try {
                Log.d(TAG, "Start cache data")
                Thread.sleep(simulateTime)
                val results = ArrayList<NewsResultEntity>()
                for (i in 0..9) {
                    val entity = NewsResultEntity()
                    entity.type = "Cache"
                    entity.desc = "Desc=$i"
                    results.add(entity)
                }
                observableEmitter.onNext(results)
                observableEmitter.onComplete()
                Log.d(TAG, "End cache data")
            } catch (e: InterruptedException) {
                if (!observableEmitter.isDisposed) {
                    observableEmitter.onError(e)
                }
            }
        }
    }
}
