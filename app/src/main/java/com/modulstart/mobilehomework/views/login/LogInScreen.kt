package com.modulstart.mobilehomework.views.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.api.TokenInterceptor
import com.modulstart.mobilehomework.presenters.LogInPresenter
import com.modulstart.mobilehomework.repository.auth.AuthProvider
import com.modulstart.mobilehomework.views.HomeScreen
import com.modulstart.mobilehomework.views.base.BaseScreen
import com.modulstart.mobilehomework.views.signup.SignupScreen
import kotlinx.android.synthetic.main.activity_main.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class LogInScreen : BaseScreen(),
    LogInView {

    @InjectPresenter
    lateinit var logInPresenter: LogInPresenter

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    @Inject
    lateinit var provider: AuthProvider



    @ProvidePresenter
    fun provideLogInPresenter(): LogInPresenter {
        return LogInPresenter(provider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        interceptorInit()
        logInPresenter.checkLogin()
        setContentView(R.layout.activity_main)
        loginButton.setOnClickListener {
            logInPresenter.logIn(login.text.toString(), password.text.toString())
        }
        signupBtn.setOnClickListener {
            startActivity(Intent(this@LogInScreen, SignupScreen::class.java))
        }

    }

    override fun showProgress() {
        Toast.makeText(applicationContext,
            "Loading", Toast.LENGTH_SHORT).show()
    }

    override fun loginSuccess(accessToken: String) {
        val sharedPref = sharedPrefs//getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString(getString(R.string.access_token), accessToken)
            putBoolean(getString(R.string.login_state), true)
            commit()
        }
        interceptorInit()
        login()
    }

    override fun login() {
        startActivity(Intent(this@LogInScreen, HomeScreen::class.java))
    }

    private fun interceptorInit() {
        val sharedPref = sharedPrefs
        val loggedIn = sharedPref.getBoolean(getString(R.string.login_state), false)
        if(loggedIn){
            val accessToken = sharedPref.getString(getString(R.string.access_token), "")
            tokenInterceptor.token = accessToken!!
        }
    }


}
