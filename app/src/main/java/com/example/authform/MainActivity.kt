package com.example.authform

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var view: View
    private lateinit var registrationForm: View
    private lateinit var loginForm: View
    private lateinit var changeFormatText: TextView
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private var isLogin: Boolean = true
        set(value) {
            loginForm.isVisible = value
            registrationForm.isVisible = !value
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view = findViewById(android.R.id.content)
        registrationForm = findViewById(R.id.registrationForm)
        loginForm = findViewById(R.id.loginForm)
        changeFormatText = findViewById(R.id.changeFormatText)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        isLogin = true
        progressBar.isVisible = false

        changeFormatText.setOnClickListener {
            isLogin = !isLogin
        }

        btnRegister.setOnClickListener {
            registerUser()
        }

        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Snackbar.make(view, "All fields are required", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            Snackbar.make(view, "Invalid username: use letters, numbers and underscore", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Snackbar.make(view, "Password too short (6 symbols minimum)", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Snackbar.make(view, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
            return
        }

        // Assuming we have a function checkUsernameAvailable
        if (checkUsernameUnavailable(username)) {
            Snackbar.make(view, "Username is already taken", Snackbar.LENGTH_SHORT).show()
            return
        }

        // Simulate server request
        btnRegister.isEnabled = false
        lifecycleScope.launch {
            progressBar.isVisible = true
            delay(3000)
            // Perform registration (e.g., send request to server)
            // Simulate server error
            if(kotlin.random.Random.nextBoolean()) {
                // On success
                SharedPreference(this@MainActivity).saveString(username, password)
                Snackbar.make(view, "Registration successful", Snackbar.LENGTH_SHORT).show()
            } else {
                // On error
                 Snackbar.make(view, "Server error. Try again later", Snackbar.LENGTH_SHORT).show()
            }
            progressBar.isVisible = false
            btnRegister.isEnabled = true
        }
    }

    private fun loginUser() {
        val username = etLoginUsername.text.toString().trim()
        val password = etLoginPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "All fields are required", Snackbar.LENGTH_SHORT).show()
            return
        }

        // Simulate server request
        btnLogin.isEnabled = false
        lifecycleScope.launch {
            progressBar.isVisible = true
            delay(3000)
            // Assuming we have a function checkLoginCredentials
            if (checkLoginCredentials(username, password)) {
                // Simulate server error
                if(kotlin.random.Random.nextBoolean()) {
                    // On success
                    Snackbar.make(view, "Login successful", Snackbar.LENGTH_SHORT).show()
                } else {
                    // On error
                    Snackbar.make(view, "Server error. Try again later", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                // On error
                Snackbar.make(view, "Invalid username or password", Snackbar.LENGTH_SHORT).show()
            }
            progressBar.isVisible = false
            btnLogin.isEnabled = true
        }
    }

    private fun checkUsernameUnavailable(username: String): Boolean {
        // Simulate a server check
        return SharedPreference(this).getString(username) != ""
    }

    private fun checkLoginCredentials(username: String, password: String): Boolean {
        // Simulate a server check
        return SharedPreference(this).getString(username) == password
    }
}
