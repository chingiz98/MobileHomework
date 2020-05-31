package com.modulstart.mobilehomework.views.transactions

import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import com.modulstart.mobilehomework.views.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TransactionsView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(transactions: MutableList<Transaction>, accounts: MutableList<Account>)
}