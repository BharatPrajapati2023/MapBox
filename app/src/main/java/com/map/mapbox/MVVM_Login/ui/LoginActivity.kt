package com.map.mapbox.MVVM_Login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.map.mapbox.MVVM_Login.Repositery.AuthRepostory
import com.map.mapbox.MVVM_Login.Utils.ApiService
import com.map.mapbox.MVVM_Login.Utils.VibrateView
import com.map.mapbox.MVVM_Login.View_Model.LoginActivityViewModel
import com.map.mapbox.MVVM_Login.View_Model.LoginActivityViewModelFactery
import com.map.mapbox.MVVM_Login.data.LoginBody
import com.map.mapbox.R
import com.map.mapbox.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.loginWithGoogleBtn.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        binding.registerBtn.setOnClickListener(this)
        binding.emailEt.onFocusChangeListener = this
        binding.passEt.setOnKeyListener(this)

        viewModel = ViewModelProvider(
            this,
            LoginActivityViewModelFactery(AuthRepostory(ApiService.getService()), application)
        ).get(LoginActivityViewModel::class.java)
        setupObsoverer()
    }

    private fun setupObsoverer() {
        viewModel.getIsLoading().observe(this) {
            binding.progressBar.isVisible = it
        }

        viewModel.getErrorMessage().observe(this) {
            //fuuname, email, password
            val fromErrorKey = arrayOf("fullName", "email", "password")
            val message = StringBuilder()
            it.map { entry ->
                if (fromErrorKey.contains(entry.key)) {
                    when (entry.key) {
                        "email" -> {
                            binding.emailInput.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "password" -> {
                            binding.passInput.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")
                }
                if (message.isNotEmpty()) {
                    AlertDialog.Builder(this).setIcon(R.drawable.ic_remove).setTitle("Information")
                        .setMessage(message)
                        .setPositiveButton("ok") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }

        }
        viewModel.getUser().observe(this) {
            if (it != null) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun validateEmail(
        shouldUpdateView: Boolean = true,
        shouldVibrateView: Boolean = true
    ): Boolean {
        var errorMessage: String? = null
        val value: String = binding.emailEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email address is invalid"
        }

        if (errorMessage != null && shouldUpdateView) {
            binding.emailInput.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(
        shouldUpdateView: Boolean = true,
        shouldVibrateView: Boolean = true
    ): Boolean {
        var errorMessage: String? = null
        val value: String = binding.passEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be 6 characters long"
        }
        if (errorMessage != null && shouldUpdateView) {
            binding.passInput.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView) VibrateView.vibrate(this@LoginActivity, this)
            }
        }
        return errorMessage == null
    }


    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.login_btn -> {
                    submitFrom()
                }

                R.id.register_btn -> {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.email_et -> {
                    if (hasFocus) {
                        if (binding.emailInput.isErrorEnabled) {
                            binding.emailInput.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()

                    }
                }

                R.id.pass_et -> {
                    if (hasFocus) {
                        if (binding.passInput.isErrorEnabled) {
                            binding.passInput.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_UP) {
            submitFrom()
        }
        return false
    }

    private fun submitFrom() {
        if (velidate()) {
            viewModel.loginUser(
                LoginBody(
                    binding.emailEt.text!!.toString(),
                    binding.passEt.text!!.toString()
                )
            )

        }
    }

    private fun velidate(): Boolean {
        var isValied = true


        if (!validateEmail(shouldVibrateView = false)) isValied = false
        if (!validatePassword(shouldVibrateView = false)) isValied = false

        if (!isValied) VibrateView.vibrate(this, binding.cardView)
        return isValied
    }
}