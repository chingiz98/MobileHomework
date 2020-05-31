package com.modulstart.mobilehomework.views.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.api.dto.ApiError
import com.modulstart.mobilehomework.interactors.memory.AccountsMemoryInteractor
import com.modulstart.mobilehomework.interactors.memory.ProfileMemoryInteractor
import com.modulstart.mobilehomework.views.login.LogInScreen
import kotlinx.android.synthetic.main.activity_home.*
import moxy.MvpAppCompatFragment
import javax.inject.Inject


abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    @Inject
    lateinit var profileMemoryInteractor: ProfileMemoryInteractor

    @Inject
    lateinit var accountsMemoryInteractor: AccountsMemoryInteractor

    override fun onAttach(context: Context) {
        App.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun showError(error: ApiError) {
        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle(R.string.error)
            setMessage(error.message)
            setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->

            })
            show()
        }
    }

    override fun logout() {
        with(sharedPrefs.edit()){
            putString(getString(com.modulstart.mobilehomework.R.string.access_token), "")
            putBoolean(getString(com.modulstart.mobilehomework.R.string.login_state), false)
            commit()
        }
        profileMemoryInteractor.clear()
        accountsMemoryInteractor.clear()
        startActivity(Intent(activity, LogInScreen::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.toolbar?.menu?.clear()
    }
}
