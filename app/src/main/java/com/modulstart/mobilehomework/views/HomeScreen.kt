package com.modulstart.mobilehomework.views

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.modulstart.mobilehomework.R
import com.modulstart.mobilehomework.views.base.BaseScreen
import kotlinx.android.synthetic.main.activity_home.*

class HomeScreen : BaseScreen() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.menu.clear()
        navView.inflateMenu(R.menu.bottom_nav)
        navController.graph.startDestination = R.id.accounts_fragment
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.accounts_fragment,
                R.id.transactionsFragment,
                R.id.chat,
                R.id.profileFragment
            )
        )
        toolbar.title = getString(R.string.accounts)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}