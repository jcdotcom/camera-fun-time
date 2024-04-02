package edu.gvsu.cis.camerafuntime

import android.content.Intent
import android.media.Image
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(
    private val imageList: List<Bitmap>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = imageList[position]
        holder.bind(image)
        holder.itemView.setOnClickListener {
            //TODO: listen for image clicks to show them full size in ViewerActivity
            //val toViewer = Intent(this, ViewerActivity::class.java)
            //toViewer.putExtra("image", image)
            //startActivity(toViewer)
        }
    }

    override fun getItemCount(): Int = imageList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.img)

        fun bind(image: Bitmap) {
            // Load the image into the ImageView
            //imageView.setImageURI(image.uri)
            imageView.setImageBitmap(image)
        }
    }
}