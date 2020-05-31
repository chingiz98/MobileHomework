package com.modulstart.mobilehomework.views.login

import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.views.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LogInView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun loginSuccess(accessToken: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun login()

}