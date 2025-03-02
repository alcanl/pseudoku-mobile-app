package com.alcanl.sudoku

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityLoginBinding
import com.alcanl.sudoku.global.WHAT_ALREADY_TAKEN_USERNAME_OR_EMAIL
import com.alcanl.sudoku.global.WHAT_EMPTY_USER_DATA
import com.alcanl.sudoku.global.WHAT_HIDE_LOADING
import com.alcanl.sudoku.global.WHAT_INVALID_USER_DATA
import com.alcanl.sudoku.global.WHAT_SERVICE_EX
import com.alcanl.sudoku.global.WHAT_SHOW_LOADING
import com.alcanl.sudoku.global.extension.setAnimation
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.service.ServiceException
import com.alcanl.sudoku.service.SudokuApplicationDataService
import com.alcanl.sudoku.viewmodel.LoginActivityListenersViewModel
import com.facebook.CallbackManager
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import javax.inject.Inject
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var applicationService: SudokuApplicationDataService

    @Inject
    lateinit var threadPool: ExecutorService

    private var mUsernameLogin = true

    private lateinit var mHandler: LoginActivityHandler

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
        mHandler = LoginActivityHandler(this)
    }
    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.viewModel = LoginActivityListenersViewModel(this)
    }
    private fun getUserInfo() : User?
    {
        val usernameOrEmail = mBinding.textViewUsername.text.toString().trim()
        val password = mBinding.editTextPassword.text.toString()

        if (usernameOrEmail.isBlank() || password.isBlank()) {
            mHandler.sendEmptyMessage(WHAT_EMPTY_USER_DATA)
            return null
        }
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
        mHandler.postDelayed( {
            Intent(this, MainActivity::class.java).apply {
                putExtras(bundle)
                startActivity(this)
                finish()
            }
        }, 2000)

    }
    private fun googleButtonAnimationListener() : AnimationListener
    {
        return object: AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                val callbackManager = CallbackManager.Factory.create()
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
            mHandler.sendEmptyMessage(WHAT_SHOW_LOADING)
            val userInfo = getUserInfo() ?: return

            val user = if (mUsernameLogin)
                applicationService.findUserByUsernameAndPassword(userInfo.username, userInfo.password)
            else
                applicationService.findUserByEmailAndPassword(userInfo.eMail, userInfo.password)

            if (user != null)
                finalizeLoginProcess(user)
            else
                mHandler.apply {
                    postDelayed( { sendEmptyMessage(WHAT_INVALID_USER_DATA)
                        sendEmptyMessage(WHAT_HIDE_LOADING) }, 2000) }

        } catch (_: ServiceException) {
            mHandler.apply {
                postDelayed({ sendEmptyMessage(WHAT_SERVICE_EX)
                    sendEmptyMessage(WHAT_HIDE_LOADING)}, 2000)
            }
        }

    }
    private fun signUpButtonClickedCallback()
    {
        mHandler.sendEmptyMessage(WHAT_SHOW_LOADING)
        val userInfo = getUserInfo() ?: return
        val result: Boolean
        try {
            result = if (mUsernameLogin)
                applicationService.checkAndSaveUserByUsername(userInfo)
            else
                applicationService.checkAndSaveUserByEmail(userInfo)

            if (result)
                finalizeLoginProcess(userInfo)
            else
                mHandler.apply {
                    postDelayed({ sendEmptyMessage(WHAT_ALREADY_TAKEN_USERNAME_OR_EMAIL)
                        sendEmptyMessage(WHAT_HIDE_LOADING)}, 2000)
                }
        } catch (_: ServiceException) {
            mHandler.apply { postDelayed({sendEmptyMessage(WHAT_SERVICE_EX)
                sendEmptyMessage(WHAT_HIDE_LOADING)}, 2000) }
        }
    }
    private class LoginActivityHandler(activity: LoginActivity) : Handler(Looper.myLooper()!!) {
        private val mWeakReference = WeakReference(activity)

        override fun handleMessage(msg: Message)
        {
            val activity = mWeakReference.get()!!
            super.handleMessage(msg)
            when (msg.what) {
                 WHAT_INVALID_USER_DATA -> {
                    Toast.makeText(activity, "Invalid Username/Email or password", Toast.LENGTH_LONG).show()
                    activity.clearEditTexts()
                }
                WHAT_EMPTY_USER_DATA -> {
                    Toast.makeText(activity, "Username or Password Must Not Be Empty", Toast.LENGTH_LONG).show()
                    activity.clearEditTexts()
                }
                WHAT_ALREADY_TAKEN_USERNAME_OR_EMAIL -> {
                    Toast.makeText(activity, "Username or Email Has Already Been Taken", Toast.LENGTH_LONG).show()
                    activity.clearEditTexts()
                }
                WHAT_SERVICE_EX -> {
                    Toast.makeText(activity, "Username already in use", Toast.LENGTH_LONG).show()
                    activity.clearEditTexts()
                }
                WHAT_SHOW_LOADING -> activity.mBinding.frameLayoutLoading.visibility = View.VISIBLE
                WHAT_HIDE_LOADING -> activity.mBinding.frameLayoutLoading.visibility = View.GONE
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