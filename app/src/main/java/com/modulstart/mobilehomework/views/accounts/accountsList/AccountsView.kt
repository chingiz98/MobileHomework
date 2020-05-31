package com.modulstart.mobilehomework.views.accounts.accountsList

import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.views.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

interface AccountsView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAccountsLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAccounts(accounts: MutableList<Account>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun createAccountSuccess(account: Account)
}