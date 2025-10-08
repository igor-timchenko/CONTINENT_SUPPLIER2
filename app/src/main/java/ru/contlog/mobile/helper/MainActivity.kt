package ru.contlog.mobile.helper

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.contlog.mobile.helper.databinding.ActivityMainBinding
import ru.contlog.mobile.helper.repo.Api

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.phoneInput.addTextChangedListener(TextInputListener { s ->
            var phoneOk = true

            if (s == null) {
                phoneOk = false
            } else {
                val phonePattern = Regex("\\+?[78]\\d{10}")
                if (!phonePattern.matches(s)) {
                    phoneOk = false
                }
            }

            binding.getCodeButton.isEnabled = phoneOk
        })

        binding.codeInput.addTextChangedListener(TextInputListener { s ->
            binding.checkCodeButton.isEnabled = s != null
        })

        binding.getCodeButton.setOnClickListener {
            getCode()
        }

        binding.checkCodeButton.setOnClickListener {
            checkCode()
        }

        binding.versionLabel.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }

    private fun getCode() {
        binding.codeSentMessage.visibility = View.INVISIBLE
        val phoneNumber = binding.phoneInput.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val result = Api.Auth.getSms(phoneNumber)
            launch(Dispatchers.Main) {
                if (!result.error) {
                    binding.codeSentMessage.visibility = View.VISIBLE
                } else {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle(getString(R.string.errorSendingSMS))
                        .setMessage(result.message)
                        .setPositiveButton(getString(R.string.ok), null)
                        .show()
                }
            }
        }
    }

    private fun checkCode() {
        val phoneNumber = binding.phoneInput.text.toString()
        val code = binding.codeInput.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val result = Api.Auth.checkSms(phoneNumber, code)
            launch(Dispatchers.Main) {
                if (!result.error) {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle(getString(R.string.errorCheckingSMS))
                        .setMessage("Успех, код подходит, в следующей версии откроем следующий экран")
                        .setPositiveButton(getString(R.string.ok), null)
                        .show()
                } else {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle(getString(R.string.errorCheckingSMS))
                        .setMessage(result.message)
                        .setPositiveButton(getString(R.string.ok), null)
                        .show()
                }
            }
        }
    }
}