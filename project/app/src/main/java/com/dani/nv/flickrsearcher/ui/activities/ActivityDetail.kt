package com.dani.nv.flickrsearcher.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.dani.nv.flickrsearcher.R
import com.dani.nv.flickrsearcher.utils.Commons
import com.flickr4java.flickr.photos.Photo
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat

class ActivityDetail : AppCompatActivity() {
    private var photoUrl: String? = null
    private var photoTitle: String? = null
    private var photoUserName: String? = null
    private var photoDate: String? = null
    private var photoDesc: String? = null


    companion object {
        val PARAM_PHOTO_URL = "param_photo_url"
        val PARAM_PHOTO_TITLE = "param_photo_title"
        val PARAM_PHOTO_AUTHOR = "param_photo_author"
        val PARAM_PHOTO_DATE = "param_photo_date"
        val PARAM_PHOTO_DESCRIPTION = "param_photo_desc"

        private val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")

        fun newIntent(ctx: Context, photo: Photo): Intent {
            val i = Intent(ctx, ActivityDetail::class.java)
            i.putExtra(PARAM_PHOTO_URL, Commons().getFlickrImageUrl(photo))
            i.putExtra(PARAM_PHOTO_TITLE, photo.title)
            i.putExtra(PARAM_PHOTO_AUTHOR, photo.owner.username)
            photo.dateTaken?.let {
                i.putExtra(PARAM_PHOTO_DATE, dateFormat.format(photo.dateTaken))
            }
            i.putExtra(PARAM_PHOTO_DESCRIPTION, photo.description)
            return i
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent?.let {
            if (it.hasExtra(PARAM_PHOTO_URL)) photoUrl = it.getStringExtra(PARAM_PHOTO_URL)
            if (it.hasExtra(PARAM_PHOTO_TITLE)) photoTitle = it.getStringExtra(PARAM_PHOTO_TITLE)
            if (it.hasExtra(PARAM_PHOTO_AUTHOR)) photoUserName = it.getStringExtra(PARAM_PHOTO_AUTHOR)
            if (it.hasExtra(PARAM_PHOTO_DATE)) photoDate = it.getStringExtra(PARAM_PHOTO_DATE)
            if (it.hasExtra(PARAM_PHOTO_DESCRIPTION)) photoDesc = it.getStringExtra(PARAM_PHOTO_DESCRIPTION)
        }

        setValuesOrEnd()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setValuesOrEnd() {
        supportActionBar?.title = photoTitle

        //photoUrl is a required field, if it's null, then finish
        photoUrl?.let { url ->
            imgContent.setImageURI(url)
            imgContent.setOnClickListener { startActivity(ActivityFullscreen.newIntent(this@ActivityDetail, url, photoTitle)) }
        } ?: run { finish() }


        showIfNotVoid(txtTitle, photoTitle, R.string.detail_no_title)
        showIfNotVoid(txtAuthor, photoUserName, R.string.detail_no_username)
        showIfNotVoid(txtDate, photoDate, R.string.detail_no_date)
        showIfNotVoid(txtDescription, photoDesc, R.string.detail_no_description)

    }

    private fun showIfNotVoid(view: TextView, value: String?, defaultValue: Int) {
        if (!value.isNullOrEmpty()) {
            view.text = value
        } else {
            view.text = getString(defaultValue)
        }
    }
}
