package com.modulstart.mobilehomework.views.base

import com.modulstart.mobilehomework.api.dto.ApiError
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun showError(error: ApiError)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun logout()
}