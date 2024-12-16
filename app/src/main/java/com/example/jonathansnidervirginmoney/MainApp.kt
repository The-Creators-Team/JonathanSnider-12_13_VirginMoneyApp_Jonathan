package com.example.jonathansnidervirginmoney

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


//main purpose of this class is to enable the application to use Hilt and its annotations
//must extend Application
@HiltAndroidApp
class MainApp: Application()