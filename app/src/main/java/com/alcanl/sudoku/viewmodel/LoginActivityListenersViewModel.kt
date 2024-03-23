package com.alcanl.sudoku.viewmodel

import com.alcanl.sudoku.LoginActivity
import java.lang.ref.WeakReference

class LoginActivityListenersViewModel(activity: LoginActivity) {
    private val mWeakReference = WeakReference(activity)

    fun handleLoginButton()
    {
        mWeakReference.get()?.loginButtonClicked()
    }
    fun handleSignUpButton()
    {
        mWeakReference.get()?.signUpButtonClicked()
    }
    fun handleGoogleButton()
    {
        mWeakReference.get()?.googleAuthenticationButtonClicked()
    }
    fun handleFacebookButton()
    {
        mWeakReference.get()?.facebookAuthenticationButtonClicked()
    }
}