package com.example.notefun2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleObserver
import com.example.notefun2.ui.Navigation
import com.example.notefun2.ui.theme.DarkBlue
import com.example.notefun2.ui.theme.NoteFun2Theme
import com.example.notefun2.ui.theme.Orange
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContent {
            NoteFun2Theme {
                // A surface container using the 'background' color from the theme
                Surface{
                    Navigation()
                }
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(color = DarkBlue, darkIcons = false)
                }
            }
        }
    }

    companion object {
        private lateinit var instance: MainActivity
        fun addLifecycleObserver(observer: LifecycleObserver){
            instance.lifecycle.addObserver(observer)
        }
    }
}

