package com.dani.nv.flickrsearcher.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dani.nv.flickrsearcher.R
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import kotlinx.android.synthetic.main.activity_fullscreen.*


class ActivityFullscreen : AppCompatActivity() {
    private var photoURL: String? = null
    private var photoTitle: String? = null

    companion object {
       const val PARAM_PHOTO_URL = "param_photo_url"
       const val PARAM_PHOTO_TITLE = "param_photo_title"

        fun newIntent(ctx: Context, url: String, title: String?): Intent {
            val i = Intent(ctx, ActivityFullscreen::class.java)
            i.putExtra(PARAM_PHOTO_URL, url)
            i.putExtra(PARAM_PHOTO_TITLE, title)
            return i
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        photoURL = intent?.extras?.getString(PARAM_PHOTO_URL)
        photoTitle = intent?.extras?.getString(PARAM_PHOTO_TITLE)

        supportActionBar?.title = photoTitle

        showImageOrEnd()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showImageOrEnd() {
        photoURL?.let {
            val ctrl = Fresco.newDraweeControllerBuilder().setUri(Uri.parse(it)).setTapToRetryEnabled(true).build()
            val hierarchy = GenericDraweeHierarchyBuilder(resources)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(ProgressBarDrawable())
                .build()
            imgContent.controller = ctrl
            imgContent.hierarchy = hierarchy
        } ?: run { finish() }
    }

}
