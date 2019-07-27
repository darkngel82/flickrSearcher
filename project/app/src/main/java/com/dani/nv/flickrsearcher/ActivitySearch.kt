package com.dani.nv.flickrsearcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_search_appbar.*


class ActivitySearch : AppCompatActivity() {

    //enum that represents the current state of the ui
    enum class ViewState {
        LOADING,
        NODATA,
        DATASHOW
    }

    private var currentState = ViewState.NODATA

    companion object {
        fun newIntent(ctx: Context): Intent {
            return Intent(ctx, ActivitySearch::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        refreshUIState(currentState)

        setUIEvents()
    }


    //change the ui state according to results
    private fun refreshUIState(state: ViewState) {
        currentState = state
        layoutContent.visibility = if (state == ViewState.DATASHOW) View.VISIBLE else View.GONE
        layoutNoContent.visibility = if (state == ViewState.NODATA) View.VISIBLE else View.GONE
        layoutLoading.visibility = if (state == ViewState.LOADING) View.VISIBLE else View.GONE
    }


    //set the events of ui objects
    private fun setUIEvents() {

        //if entered more than 2 chars, make search, else show "no data"
        editQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //after text is changed, if it's not empty, show the delete drawable
                s?.let {
                    if (it.isEmpty()) editQuery.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    else {
                        editQuery.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.baseline_cancel_black_24, 0
                        )
                        if (it.length > 2) {
                            runSearch()
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
                        editQuery.setText("")
                        refreshUIState(ViewState.NODATA)
                        v?.onTouchEvent(event) ?: true
                    }
                }
            }

            v?.onTouchEvent(event) ?: false
        }
    }

    private fun runSearch() {
        refreshUIState(ViewState.LOADING)

        //for debug purposes
        Handler().postDelayed({
            refreshUIState(ViewState.NODATA)
        }, 3000)
    }
}
