package com.dani.nv.flickrsearcher.interfaces

import com.flickr4java.flickr.photos.Photo

interface ItemClickListener {

    //used to return photo clicked
    abstract fun itemClicked(photo: Photo)
}