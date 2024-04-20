package edu.gvsu.cis.camerafuntime

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.gvsu.cis.camerafuntime.databinding.ActivityGalleryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import java.net.URI

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var viewModel: GalleryActivityViewModel
    private lateinit var galleryRecycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[GalleryActivityViewModel::class.java]
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        viewModel.setSavePath(intent.getStringExtra("savePath"))
        galleryRecycler = binding.galleryRecycler
        setContentView(binding.root)

        binding.backBtn2.setOnClickListener {
            finish()
        }
    }

    override fun onResume(){
        super.onResume()
        galleryRecycler.layoutManager = GridLayoutManager(this, 3)

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val imageList = mutableListOf<Bitmap>()
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), intent.getStringExtra("savePath"))

            if(directory.listFiles().isNullOrEmpty()){
                Snackbar.make(binding.root, "No images found at $directory", Snackbar.LENGTH_SHORT).show()
            }

            Log.d("GalleryActivity", directory.toString())
            directory.listFiles()?.filter { it.isFile && it.extension == "jpg" }?.forEach {
                Glide.with(this@GalleryActivity)
                    .asBitmap()
                    .load(it)
                    .override(150,200)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            imageList.add(resource)
                            galleryRecycler.adapter = ImageAdapter(imageList)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })

            }
        }
    }
}