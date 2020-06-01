package com.modulstart.mobilehomework.views.accounts.actions

import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.views.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

interface AccountActionsView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAccount(account: Account)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTransferDialog(accounts: MutableList<Account>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun depositSuccess(amount: BigDecimal)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun transactionSuccess(fromId: Long, toId: Long, comment: String, amount: BigDecimal)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun accountCloseSuccess()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTemplatesDialog(templates: List<TemplateDB>)

}