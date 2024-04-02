package edu.gvsu.cis.camerafuntime

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.gvsu.cis.camerafuntime.databinding.ActivityViewerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewerBinding
    //private lateinit var viewModel: ViewerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = ViewModelProvider(this)[GalleryActivityViewModel::class.java]
        binding = ActivityViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn3.setOnClickListener {
            finish()
        }
    }

    override fun onResume(){
        super.onResume()

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val image = binding.imageView
            //TODO: Display selected image here
            /*
            Glide.with(this@ViewerActivity)
                .asBitmap()
                .load(it)
                .override(150,200)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        image.setImageBitmap(resource)
                        //galleryRecycler.adapter = ImageAdapter(imageList)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
            */
            }
        }
    }