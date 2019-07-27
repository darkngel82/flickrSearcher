package com.dani.nv.flickrsearcher.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import com.dani.nv.flickrsearcher.R
import com.dani.nv.flickrsearcher.constants.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_search_appbar.*
import android.util.Log
import com.dani.nv.flickrsearcher.ui.widgets.CustomDiag
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.PhotoList
import com.flickr4java.flickr.photos.SearchParameters
import android.support.v7.widget.GridLayoutManager
import com.dani.nv.flickrsearcher.adapters.FlickrImageAdapter
import com.dani.nv.flickrsearcher.utils.Commons
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_search_content.*


class ActivitySearch : AppCompatActivity() {

    //enum that represents the current state of the ui
    enum class ViewState {
        LOADING,
        NODATA,
        DATASHOW
    }

    private var photos: PhotoList<Photo>? = null
    private var currentState = ViewState.NODATA
    private var queryString: String = ""

    private var searchDisposable: Disposable? = null

    companion object {
        fun newIntent(ctx: Context): Intent {
            return Intent(ctx, ActivitySearch::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        refreshUIState(currentState)
        initPhotoArray()
        setUIEvents()
    }

    //init the Photolist to void state
    private fun initPhotoArray() {
        photos = PhotoList()
        rvContent.layoutManager = GridLayoutManager(this, 3)
    }


    //change the ui state according to results
    private fun refreshUIState(state: ViewState) {
        this@ActivitySearch?.runOnUiThread {
            //context not null, then run on UI thread

            currentState = state
            layoutContent.visibility = if (state == ViewState.DATASHOW) View.VISIBLE else View.GONE
            layoutNoContent.visibility = if (state == ViewState.NODATA) View.VISIBLE else View.GONE
            layoutLoading.visibility = if (state == ViewState.LOADING) View.VISIBLE else View.GONE
        }
    }


    //set the events of ui objects
    private fun setUIEvents() {

        //if entered more than 2 chars, make search, else show "no data"
        editQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //after text is changed, if it's not empty, show the delete drawable
                searchDisposable?.dispose()

                s?.let { queryStr ->
                    if (queryStr.isEmpty()) editQuery.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    else {
                        editQuery.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.baseline_cancel_black_24, 0
                        )
                        if (queryStr.length > 2) {

                            queryString = queryStr.toString()
                            searchByStringRX()
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        //sets edit clear button
        editQuery.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            editQuery.compoundDrawables[DRAWABLE_RIGHT]?.let { drawableRight ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (editQuery.right - drawableRight.bounds.width())) {
                        searchDisposable?.dispose()
                        editQuery.setText("")
                        refreshUIState(ViewState.NODATA)
                        v?.onTouchEvent(event) ?: true
                    }
                }
            }

            v?.onTouchEvent(event) ?: false
        }
    }

    private fun searchByStringRX() {
        refreshUIState(ViewState.LOADING)
        if(Commons().isNetworkAvailable(this@ActivitySearch)){

            searchDisposable = Observable.fromCallable {
                try {
                    val apiKey = Constants().flickrKey
                    val sharedSecret = Constants().flickrSecret
                    val rest = REST()
                    val flickrClient = Flickr(apiKey, sharedSecret, rest)

                    val searchParameters = SearchParameters()
                    searchParameters.text = queryString
                    searchParameters.extras = mutableSetOf<String>("url_o")
                    photos = flickrClient.photosInterface.search(searchParameters, 0, 1)
                } catch (ex: Exception) {
                    refreshUIState(ViewState.NODATA)
                    ex.localizedMessage?.let {
                        errorReceived(ex.localizedMessage)
                    } ?: run {
                        errorReceived(getString(R.string.diag_error_appear))
                    }
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    showValues()
                }

        }else{
            errorReceived(getString(R.string.diag_error_no_internet))
        }

    }
    
    private fun showValues() {
        photos?.let { photoList ->
            if (photoList.isEmpty()) refreshUIState(ViewState.NODATA)
            else {
                rvContent.adapter = FlickrImageAdapter(photoList)
                refreshUIState(ViewState.DATASHOW)
            }
        }
    }

    private fun errorReceived(message: String) {
        Log.d(this@ActivitySearch::javaClass.name, message)
        CustomDiag().showErrorDialog(this@ActivitySearch, message)
    }


}
