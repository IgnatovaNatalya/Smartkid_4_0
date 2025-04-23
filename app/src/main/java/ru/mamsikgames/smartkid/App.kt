package ru.mamsikgames.smartkid

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import ru.mamsikgames.smartkid.di.DatabaseModule
import ru.mamsikgames.smartkid.di.interactorModule
import ru.mamsikgames.smartkid.di.repositoryModule
import ru.mamsikgames.smartkid.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(DatabaseModule, repositoryModule, interactorModule, viewModelModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}