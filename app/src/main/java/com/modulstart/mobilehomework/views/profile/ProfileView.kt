package com.modulstart.mobilehomework.views.profile

import android.graphics.Bitmap
import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.User
import com.modulstart.mobilehomework.views.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

interface ProfileView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProfile(user: User)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProfileImage(bitmap: Bitmap)

}