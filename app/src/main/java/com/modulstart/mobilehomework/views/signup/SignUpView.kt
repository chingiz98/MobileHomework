package com.modulstart.mobilehomework.views.signup

import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.views.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SignUpView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun signupSuccess(accessToken: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun login()

}