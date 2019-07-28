package com.dani.nv.flickrsearcher.adapters

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.AdapterView
import com.dani.nv.flickrsearcher.R
import com.dani.nv.flickrsearcher.interfaces.ItemClickListener
import com.dani.nv.flickrsearcher.utils.Commons
import com.facebook.drawee.view.SimpleDraweeView
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.PhotoList

class FlickrImageAdapter(private val photos: PhotoList<Photo>, private val itemClickListener: ItemClickListener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FlickrImageAdapter.FlickrViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): FlickrViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.li_search_item, viewGroup, false)
        return FlickrViewHolder(v)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(viewHolder: FlickrViewHolder, p1: Int) {
         photos[p1]?.let { photo ->
            viewHolder.imgFlickr.setImageURI(Commons().getFlickrImageUrl(photo))
             viewHolder.itemView.setOnClickListener {
                 itemClickListener.itemClicked(photo)
             }
        }
    }

    class FlickrViewHolder internal constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        internal var cv: androidx.cardview.widget.CardView = itemView.findViewById(R.id.cvResult)
        internal var imgFlickr: SimpleDraweeView = itemView.findViewById(R.id.imgResult)

    }
}