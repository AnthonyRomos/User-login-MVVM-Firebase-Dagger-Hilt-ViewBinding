package com.thomy.mvvm_firebase_login.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.thomy.mvvm_firebase_login.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        screenSplash.setKeepOnScreenCondition { false }
    }

    private fun initUI() {
        initNavigation()

    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onBackPressed() {
        val currentDestination = navController.currentDestination

        if (currentDestination?.id == R.id.loginFragment) {
            moveTaskToBack(true)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}