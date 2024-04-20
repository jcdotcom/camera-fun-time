package edu.gvsu.cis.camerafuntime

import android.Manifest
import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import edu.gvsu.cis.camerafuntime.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA

    private lateinit var img: ImageView

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    Log.d("DEBUG","!!! PERMISSION FAILURE k: ${it.key} - v: ${it.value}")
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.setSavePath(it.data?.getStringExtra("savePath"))
                Snackbar.make(
                    binding.root,
                    "Settings Saved!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

    // --- [ Click Listeners ] --- //

        binding.captureBtn.setOnClickListener {
            shutterImage()
            takePhoto()
        }
        binding.flipBtn.setOnClickListener {
            flipCamera()
        }
        binding.galleryBtn.setOnClickListener {
            val toGallery = Intent(this, GalleryActivity::class.java)
            toGallery.putExtra("savePath", viewModel.getSavePath())
            startActivity(toGallery)
        }
        binding.settingsBtn.setOnClickListener {
            val toSettings = Intent(this, SettingsActivity::class.java)
            toSettings.putExtra("savePath", viewModel.getSavePath())
            settingsLauncher.launch(toSettings)
        }

    // --- [ Camera Executor ] --- //

        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview, imageCapture, imageAnalyzer)
            }
            catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun requestPermissions() {
        permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun flipCamera(){
        if(lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA){ lensFacing = CameraSelector.DEFAULT_BACK_CAMERA }
        else if (lensFacing == CameraSelector.DEFAULT_BACK_CAMERA){ lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA }
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, viewModel.getSavePath())
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    private fun shutterImage() {
        binding.shutterView.setImageResource(R.mipmap.white)
        val fadeOutAnimator = ValueAnimator.ofFloat(1f, 0f)
        fadeOutAnimator.duration = 500
        fadeOutAnimator.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Float
            binding.shutterView.alpha = alpha
        }
        fadeOutAnimator.start()
    }

    companion object {
        private const val REQUEST_CODE_SETTINGS = 1
        private const val TAG = "CameraFunTime"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}



