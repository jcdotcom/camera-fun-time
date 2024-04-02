package edu.gvsu.cis.camerafuntime

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.gvsu.cis.camerafuntime.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsActivityViewModel::class.java]
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: directory for saving and loading?

        binding.backBtn.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(
                "snack",
                "Snackbar"
            )
            //returnIntent.putExtra("minLength", minSeek.progress)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
}