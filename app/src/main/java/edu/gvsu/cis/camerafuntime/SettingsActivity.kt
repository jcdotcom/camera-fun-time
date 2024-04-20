package edu.gvsu.cis.camerafuntime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import edu.gvsu.cis.camerafuntime.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsActivityViewModel::class.java]
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savePath = intent.getStringExtra("savePath")
        viewModel.setSavePath(savePath ?: "test")

        binding.savePathField.setText(viewModel.getSavePath())

        binding.backBtn.setOnClickListener {
            if(binding.savePathField.text.toString() != "") {
                val returnIntent = Intent()
                returnIntent.putExtra(
                    "savePath",
                    binding.savePathField.text.toString()
                )
                setResult(RESULT_OK, returnIntent)
                finish()
            }
            else{
                Snackbar.make(
                    binding.root,
                    "Save path cannot be empty!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}