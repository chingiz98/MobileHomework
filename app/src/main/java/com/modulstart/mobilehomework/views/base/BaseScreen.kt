package com.modulstart.mobilehomework.views.base

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.api.dto.ApiError
import com.modulstart.mobilehomework.interactors.memory.AccountsMemoryInteractor
import com.modulstart.mobilehomework.interactors.memory.ProfileMemoryInteractor
import com.modulstart.mobilehomework.views.login.LogInScreen
import moxy.MvpAppCompatActivity
import javax.inject.Inject

abstract class BaseScreen : MvpAppCompatActivity(), BaseView {

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    @Inject
    lateinit var profileMemoryInteractor: ProfileMemoryInteractor

    @Inject
    lateinit var accountsMemoryInteractor: AccountsMemoryInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun showError(error: ApiError) {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(R.string.error)
            setMessage(error.message)
            setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            })
            show()
        }
    }

    override fun logout(){
        with(sharedPrefs.edit()){
            putString(getString(R.string.access_token), "")
            putBoolean(getString(R.string.login_state), false)
            commit()
        }
        profileMemoryInteractor.clear()
        accountsMemoryInteractor.clear()
        startActivity(Intent(this@BaseScreen, LogInScreen::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}