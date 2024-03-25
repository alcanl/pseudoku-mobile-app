package com.alcanl.sudoku

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityLoginBinding
import com.alcanl.sudoku.global.setAnimation
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.service.ServiceException
import com.alcanl.sudoku.service.SudokuApplicationDataService
import com.alcanl.sudoku.viewmodel.LoginActivityListenersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import javax.inject.Inject
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var applicationService: SudokuApplicationDataService
    @Inject
    lateinit var threadPool: ExecutorService
    private var mUsernameLogin = true
    private lateinit var mBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        initialize()
    }
    private fun initBackgroundAnimation()
    {
        val animatedDrawable = mBinding.layoutMain.background as AnimationDrawable
        animatedDrawable.apply {
            setEnterFadeDuration(2500)
            setExitFadeDuration(4000)
            start()
        }
    }
    private fun initialize()
    {
        initBinding()
        initBackgroundAnimation()
    }
    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.viewModel = LoginActivityListenersViewModel(this)
    }
    private fun getUserInfo() : User
    {
        val usernameOrEmail = mBinding.textViewUsername.text.toString().trim()
        val password = mBinding.editTextPassword.text.toString()
        if (usernameOrEmail.contains("@")) {
            val dotCount = usernameOrEmail.substringAfter("@").count { it == '.' }
            if (dotCount == 1) {
                mUsernameLogin = false
                return User(eMail = usernameOrEmail, password = password)
            }
        }

        return User(username = usernameOrEmail, password = password)
    }
    private fun clearEditTexts()
    {
        mBinding.apply {
            editTextPassword.setText("")
            textViewUsername.setText("")
        }
    }
    private fun finalizeLoginProcess(user: User)
    {
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        Intent(this, MainActivity::class.java).apply {
            putExtras(bundle)
            startActivity(this)
            finish()
        }
    }
    private fun googleButtonAnimationListener() : AnimationListener
    {
        return object: AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animation?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        }
    }
    private fun facebookButtonAnimationListener() : AnimationListener
    {
        return object: AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animation?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        }
    }
    private fun loginButtonClickedCallback()
    {
        try {
            val userInfo = getUserInfo()
            val user = if (mUsernameLogin)
                applicationService.findUserByUsernameAndPassword(userInfo.username, userInfo.password)
            else
                applicationService.findUserByEmailAndPassword(userInfo.eMail, userInfo.password)

            if (user != null)
                finalizeLoginProcess(user)
            else
                runOnUiThread {
                    Toast.makeText(this, "Invalid Username/Email or password", Toast.LENGTH_LONG).show()
                    clearEditTexts()
                }
        } catch (_: ServiceException) {
            runOnUiThread {
                Toast.makeText(this, "Unknown Problem Occurred", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun signUpButtonClickedCallback()
    {
        val userInfo = getUserInfo()

        try {
            if (mUsernameLogin)
                applicationService.checkAndSaveUserByUsername(userInfo)
            else
                applicationService.checkAndSaveUserByEmail(userInfo)

            finalizeLoginProcess(userInfo)
        } catch (_: ServiceException) {
            runOnUiThread {
                Toast.makeText(this, "Username already in use", Toast.LENGTH_LONG).show()
                clearEditTexts()
            }
        }
    }
    fun loginButtonClicked()
    {
        threadPool.execute(this::loginButtonClickedCallback)
    }
    fun signUpButtonClicked()
    {
        threadPool.execute(this::signUpButtonClickedCallback)
    }
    fun googleAuthenticationButtonClicked()
    {
        mBinding.buttonGoogleAuth.setAnimation(this, googleButtonAnimationListener())
    }
    fun facebookAuthenticationButtonClicked()
    {
        mBinding.buttonFacebookAuth.setAnimation(this, facebookButtonAnimationListener())
    }
}