package com.example.serviconnect.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.serviconnect.MainActivity
import com.example.serviconnect.R
import com.example.serviconnect.databinding.ActivitySplashBinding
import com.example.serviconnect.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(1500)
            checkUserSession()
        }
    }

    private suspend fun checkUserSession() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            navigateToMain(null)
        } else {
            try {
                val document = db.collection(Constants.COLLECTION_USERS)
                    .document(currentUser.uid)
                    .get()
                    .await()

                val role = document.getString("role") ?: Constants.ROLE_SEEKER
                navigateToMain(role)
            } catch (e: Exception) {
                navigateToMain(null)
            }
        }
    }

    private fun navigateToMain (role: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ROLE",role)
        startActivity(intent)
        finish()
    }
}