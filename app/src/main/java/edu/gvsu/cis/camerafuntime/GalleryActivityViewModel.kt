package edu.gvsu.cis.camerafuntime

import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.Request;
import okhttp3.internal.http2.Http2Reader
import java.io.File
import java.io.IOException
import kotlin.random.Random
import java.util.concurrent.Executors

class GalleryActivityViewModel : ViewModel() {

    private val savePath = MutableLiveData<String>()

    fun setSavePath(path: String?) {
        savePath.value = path
    }

    fun getSavePath(): String? {
        return savePath.value
    }
}