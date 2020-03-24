package com.example.myrxsample.utils.ui

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxDialog {


    @JvmStatic
    fun showErrorDialog(context: Context, message: String): Single<Boolean> {
        return Single
            .create<Boolean> { emitter ->
                AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Exception:$message,retry?？")
                    .setCancelable(false)
                    .setPositiveButton("Retry") { _, _ ->
                        Toast.makeText(context, "Request", Toast.LENGTH_SHORT).show()
                        emitter.onSuccess(true)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show()
                        emitter.onSuccess(false)
                    }
                    .show()
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
    }

}