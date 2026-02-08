package com.example.ngasiryuk

import android.app.Application
import com.example.ngasiryuk.di.AppContainer

class NgasiryukApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}

