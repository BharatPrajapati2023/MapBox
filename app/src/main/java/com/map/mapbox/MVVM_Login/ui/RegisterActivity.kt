package com.map.mapbox.MVVM_Login.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.map.mapbox.MVVM_Login.View_Model.RegisterActivityViewModel
import com.map.mapbox.MVVM_Login.View_Model.RegisterActivityViewModelFactery
import com.map.mapbox.MVVM_Login.data.RegisterBody
import com.map.mapbox.MVVM_Login.data.ValidateEmaiBody
import com.map.mapbox.R
import com.map.mapbox.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener, TextWatcher {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.nameEt.onFocusChangeListener = this
        binding.passEt.onFocusChangeListener = this
        binding.emailEt.onFocusChangeListener = this
        binding.passRe.onFocusChangeListener = this
        binding.passRe.setOnKeyListener(this)
        binding.passRe.addTextChangedListener(this)
        binding.register.setOnClickListener(this)

        viewModel = ViewModelProvider(
            this,
            RegisterActivityViewModelFactery(AuthRepostory(ApiService.getService()), application)
        ).get(RegisterActivityViewModel::class.java)
        setupObsoverer()
    }

    private fun setupObsoverer() {
        viewModel.getIsLoading().observe(this) {
            binding.progressBar.isVisible = it
        }

        viewModel.getisUniqueEmail().observe(this) {
            if (validateEmail(shouldUpdateView = false)) {
                if (it) {
                    binding.emailInput.apply {
                        if (isErrorEnabled) isErrorEnabled = false
                        setStartIconDrawable(R.drawable.ic_checked)
                        setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                    }
                } else {
                    binding.emailInput.apply {
                        if (startIconDrawable != null) startIconDrawable = null
                        isErrorEnabled = true
                        error = "Email is already taken"
                    }
                }
            }
        }


        viewModel.getErrorMessage().observe(this) {
            //fuuname, email, password
            val fromErrorKey = arrayOf("fullName", "email", "password")
            val message = StringBuilder()
            it.map { entry ->
                if (fromErrorKey.contains(entry.key)) {
                    when (entry.key) {
                        "fullName" -> {
                            binding.nameInput.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

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

    private fun validateFullName(shouldVibrateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = binding.nameEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Full Name is required"
        }
        if (errorMessage != null) {
            binding.nameInput.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView) VibrateView.vibrate(this@RegisterActivity, this)
            }
        }
        return errorMessage == null
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
                if (shouldVibrateView) VibrateView.vibrate(this@RegisterActivity, this)
            }
        }
        return errorMessage == null
    }

    private fun validateRePassword(
        shouldUpdateView: Boolean = true,
        shouldVibrateView: Boolean = true
    ): Boolean {
        var errorMessage: String? = null
        val value: String = binding.passRe.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Re-Password is required"
        } else if (value.length < 6) {
            errorMessage = "Re-Password must be 6 characters long"
        }
        if (errorMessage != null && shouldUpdateView) {
            binding.passInputRe.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView) VibrateView.vibrate(this@RegisterActivity, this)
            }
        }

        return errorMessage == null
    }

    private fun validatePassAndRePassword(
        shouldUpdateView: Boolean = true,
        shouldVibrateView: Boolean = true
    ): Boolean {
        var errorMessage: String? = null
        val pass: String = binding.passEt.text.toString()
        val passRe: String = binding.passRe.text.toString()
        if (pass != passRe) {
            errorMessage = "Confirm password doesn't match with Re-Password"
        }

        if (errorMessage != null && shouldUpdateView) {
            binding.passInputRe.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView) VibrateView.vibrate(this@RegisterActivity, this)
            }
        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if (view != null && view.id == R.id.register) onSubmit()

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.name_et -> {
                    if (hasFocus) {
                        if (binding.nameInput.isErrorEnabled) {
                            binding.nameInput.isErrorEnabled = false
                        }
                    } else {
                        validateFullName()
                    }
                }

                R.id.email_et -> {
                    if (hasFocus) {
                        if (binding.emailInput.isErrorEnabled) {
                            binding.emailInput.isErrorEnabled = false
                        }
                    } else {
                        if (validateEmail()) {
                            viewModel.validateEmailAddress(ValidateEmaiBody(binding.emailEt.text!!.toString()))
                        }

                    }
                }

                R.id.pass_et -> {
                    if (hasFocus) {
                        if (binding.passInput.isErrorEnabled) {
                            binding.passInput.isErrorEnabled = false
                        }
                    } else {
                        if (validatePassword() && binding.passEt.text!!.isNotEmpty() && validateRePassword() && validatePassAndRePassword()) {
                            if (binding.passInputRe.isErrorEnabled) {
                                binding.passInputRe.isErrorEnabled = false
                            }
                            binding.passInputRe.apply {
                                setStartIconDrawable(R.drawable.ic_checked)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }

                R.id.pass__re -> {
                    if (hasFocus) {
                        if (binding.passInputRe.isErrorEnabled) {
                            binding.passInputRe.isErrorEnabled = false
                        }
                    } else {
                        if (validateRePassword() && validatePassword() && validatePassAndRePassword()) {
                            if (binding.passInput.isErrorEnabled) {
                                binding.passInput.isErrorEnabled = false
                            }
                            binding.passInputRe.apply {
                                setStartIconDrawable(R.drawable.ic_checked)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }

            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP) {
            onSubmit()
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (validatePassword(shouldUpdateView = false) && validateRePassword(shouldUpdateView = false) &&
            validatePassAndRePassword(shouldUpdateView = false)
        ) {
            binding.passInput.apply {
                if (isErrorEnabled) isErrorEnabled = false
                setStartIconDrawable(R.drawable.ic_checked)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        } else {
            if (binding.passInput.startIconDrawable != null)
                binding.passInput.startIconDrawable = null
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    private fun onSubmit() {
        if (velidate()) {
            //make api request
            viewModel.registerUser(
                RegisterBody(
                    binding.nameEt.text!!.toString(), binding.emailEt.text!!.toString(),
                    binding.passEt.text!!.toString()
                )
            )
        }
    }

    private fun velidate(): Boolean {
        var isValied = true

        if (!validateFullName(shouldVibrateView = false)) isValied = false
        if (!validateEmail(shouldVibrateView = false)) isValied = false
        if (!validatePassword(shouldVibrateView = false)) isValied = false
        if (!validateRePassword(shouldVibrateView = false)) isValied = false
        if (isValied && !validatePassAndRePassword(shouldVibrateView = false)) isValied = false

        if (!isValied) VibrateView.vibrate(this, binding.cardView)
        return isValied
    }
}