package com.nafi.airseat.presentation.otpresetpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityOtpResetPasswordBinding
import com.nafi.airseat.presentation.resetpassword.ResetPasswordActivity
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import com.nafi.airseat.utils.showSnackBarSuccess
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpResetPasswordBinding

    private val otpResetPasswordViewModel: OtpResetPasswordViewModel by viewModel()
    private var countDownTimer: CountDownTimer? = null
    private val timerDuration = 60000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()

        val email = intent.getStringExtra("email")
        binding.textEmail.text = email

        binding.otpview.setText("")
        binding.otpview.setOtpCompletionListener { code ->
            hideKeyboard()

            if (email != null) {
                navigateToResetPassword(code, email)
            }
        }
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer =
            object : CountDownTimer(timerDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    binding.textResendOTP.text = getString(R.string.text_resend_otp, secondsRemaining)
                }

                override fun onFinish() {
                    binding.textNewCodeOTP.isVisible = true
                    binding.textResendOTP.isVisible = false

                    binding.textInfoOTP.isVisible = true
                }
            }.start()
        binding.textNewCodeOTP.isVisible = false
        binding.textResendOTP.isVisible = true

        binding.textInfoOTP.isVisible = false
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE)as InputMethodManager

        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun setClickListeners() {
        binding.textNewCodeOTP.setOnClickListener {
            val email = binding.textEmail.text.toString()
            resendOtpChangePassword(email)
        }
    }

    private fun resendOtpChangePassword(email: String) {
        otpResetPasswordViewModel.reqChangePasswordByEmailResendOtp(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.textNewCodeOTP.isVisible = true
                    showSnackBarSuccess("$it")
                    startTimer()
                },
                doOnError = {
                    binding.textNewCodeOTP.isVisible = true
                    if (it.exception is ApiErrorException) {
                        showSnackBarError("$it")
                    } else if (it.exception is NoInternetException) {
                        showSnackBarError("No Internet, Please Check Your Connection")
                    }
                },
                doOnLoading = {
                    binding.textNewCodeOTP.isVisible = false
                },
            )
        }
    }

    private fun navigateToResetPassword(
        code: String,
        email: String,
    ) {
        startActivity(
            Intent(this, ResetPasswordActivity::class.java).apply { // Replace with actual target activity
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("code", code)
                putExtra("email", email)
            },
        )
    }
}
