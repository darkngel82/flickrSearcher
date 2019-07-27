package com.dani.nv.flickrsearcher.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Commons {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        activeNetwork?.let {
            return it.isConnected
        } ?: run {
            return false
        }
    }
}