package com.kmbisset89

import android.app.Application
import com.kmbisset89.ui.viewmodel.AndroidStateHandler

/**
 * Example application class that registers the [AndroidStateHandler] to the application lifecycle.
 */
class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Register the AndroidStateHandler to the application lifecycle
        registerActivityLifecycleCallbacks(AndroidStateHandler)
    }
}