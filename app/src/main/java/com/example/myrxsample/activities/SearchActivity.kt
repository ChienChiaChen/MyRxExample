package com.example.myrxsample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.example.myrxsample.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private lateinit var mPublishSubject: PublishSubject<String>
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        et_search.doAfterTextChanged { editable -> mPublishSubject.onNext(editable!!.toString()) }
        mPublishSubject = PublishSubject.create()

        mCompositeDisposable.add(mPublishSubject
            .filter(String::isNotEmpty)
            .debounce(200, TimeUnit.MILLISECONDS)
            .switchMap { query ->
                return@switchMap Observable.create<String> { emitter ->
                    Thread.sleep(1000)
                    emitter.onNext("Search done:: $query")
                    emitter.onComplete()
                }.subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tv_search_result.text = it
            })
    }
}
