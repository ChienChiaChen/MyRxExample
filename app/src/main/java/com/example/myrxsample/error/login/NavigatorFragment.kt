package com.example.myrxsample.error.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class NavigatorFragment : Fragment() {

    private lateinit var resultSubject: PublishSubject<Boolean>
    private lateinit var cancelSubject: PublishSubject<Boolean>
    private val attachSubject = PublishSubject.create<Boolean>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachSubject.onNext(true)
        attachSubject.onComplete()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val loginSuccess = data.getBooleanExtra(LoginActivity.EXTRA_SUCCESS, false)
            resultSubject.onNext(loginSuccess)
            resultSubject.onComplete()
        } else {
            cancelSubject.onNext(true)
        }
    }

    private fun startLoginSingle(): Single<Boolean> {
        resultSubject = PublishSubject.create()
        cancelSubject = PublishSubject.create()
        startLogin() // <- 轉場
        return resultSubject
            .takeUntil(cancelSubject)
            .single(false)
    }

    private fun startLogin() {
        if (!isAdded) {
            attachSubject.subscribe { startLoginForResult() }
        } else {
            startLoginForResult()
        }
    }

    private fun startLoginForResult() =
        startActivityForResult(Intent(context, LoginActivity::class.java), 1)

    companion object {

        private const val TAG = "NavigatorFragment"

        fun startLoginForResult(activity: FragmentActivity): Single<Boolean> {
            val fragmentManager = activity.supportFragmentManager
            val fragment = fragmentManager.findFragmentByTag(TAG)
            return createNavigatorFragmentObservable(fragment, fragmentManager)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
        }

        private fun createNavigatorFragmentObservable(
            fragment: Fragment?,
            fragmentManager: FragmentManager
        ): Single<Boolean> {
            return when (fragment == null) { // 是否有產生過 NavigatorFragment.
                true -> {
                    val transaction = fragmentManager.beginTransaction()
                    NavigatorFragment()
                        .run {
                            transaction.add(this, TAG).commitAllowingStateLoss()
                            startLoginSingle()
                        }

                }
                false -> {
                    val navigatorFragment = fragment as NavigatorFragment
                    navigatorFragment.startLoginSingle()
                }
            }
        }
    }
}
