package com.modulstart.mobilehomework.views.signup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.api.TokenInterceptor
import com.modulstart.mobilehomework.presenters.SignupPresenter
import com.modulstart.mobilehomework.repository.auth.AuthProvider
import com.modulstart.mobilehomework.views.HomeScreen
import com.modulstart.mobilehomework.views.base.BaseScreen
import com.modulstart.mobilehomework.views.login.LogInScreen
import kotlinx.android.synthetic.main.activity_main.login
import kotlinx.android.synthetic.main.activity_main.password
import kotlinx.android.synthetic.main.activity_signup.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SignupScreen : BaseScreen(),
    SignUpView {
    @InjectPresenter
    lateinit var signupPresenter: SignupPresenter
    @Inject
    lateinit var tokenInterceptor: TokenInterceptor
    @Inject
    lateinit var provider: AuthProvider

    @ProvidePresenter
    fun provideSignupPresenter(): SignupPresenter {
        return SignupPresenter(provider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signInBtn.setOnClickListener {
            startActivity(Intent(this@SignupScreen, LogInScreen::class.java))
        }
        signupButton.setOnClickListener {
            if(password.text.toString() != passwordConfirm.text.toString()){
                val builder = AlertDialog.Builder(this)
                with(builder)
                {
                    setTitle(resources.getString(R.string.error))
                    setMessage(resources.getString(R.string.password_warning))
                    setPositiveButton(resources.getString(android.R.string.yes), DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    })
                    show()
                }
            } else {
                signupPresenter.signup(login.text.toString(), password.text.toString(), name.text.toString())
            }
        }
    }

    override fun showProgress() {
        Toast.makeText(applicationContext,
            "Loading", Toast.LENGTH_SHORT).show()
    }

    override fun signupSuccess(accessToken: String) {
        val sharedPref = sharedPrefs
        with(sharedPref.edit()){
            putString(getString(R.string.access_token), accessToken)
            putBoolean(getString(R.string.login_state), true)
            commit()
        }
        login()
    }

    override fun login() {
        interceptorInit()
        startActivity(Intent(this@SignupScreen, HomeScreen::class.java))
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
