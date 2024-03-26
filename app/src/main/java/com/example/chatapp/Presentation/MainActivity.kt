package com.example.chatapp.Presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.example.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etUsername.doAfterTextChanged {
            val username = it.toString()
            binding.btnProceed.isEnabled = username.isNotEmpty()
        }
        binding.btnProceed.setOnClickListener {
            val username = binding.etUsername.text.toString()
            if (username.isNotEmpty()) {
                val intent = Intent(this, chatactivity::class.java)
                intent.putExtra(chatactivity.USERNAME, username)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etUsername.requestFocus()
    }
}