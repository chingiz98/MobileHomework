package com.modulstart.mobilehomework.di.components

import com.modulstart.mobilehomework.di.modules.ApiModule
import com.modulstart.mobilehomework.views.accounts.accountsList.AccountsFragment
import com.modulstart.mobilehomework.views.accounts.actions.AccountActionsFragment
import com.modulstart.mobilehomework.views.base.BaseFragment
import com.modulstart.mobilehomework.views.login.LogInScreen
import com.modulstart.mobilehomework.views.base.BaseScreen
import com.modulstart.mobilehomework.views.profile.ProfileFragment
import com.modulstart.mobilehomework.views.transactions.TransactionsFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(ApiModule::class))

@Singleton
interface AppComponent {
    fun inject (activity: BaseScreen)
    fun inject (activity: LogInScreen)
    fun inject (fragment: AccountsFragment)
    fun inject (fragment: AccountActionsFragment)
    fun inject (fragment: TransactionsFragment)
    fun inject (fragment: ProfileFragment)
    fun inject (fragment: BaseFragment)

}