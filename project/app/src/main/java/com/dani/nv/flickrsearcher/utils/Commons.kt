package com.dani.nv.flickrsearcher.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.flickr4java.flickr.photos.Photo

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
    fun getFlickrImageUrl(photo: Photo): String {
        //flicker api doesn't return the image url, and it must be build
        return "https://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret + ".jpg"
    }

}