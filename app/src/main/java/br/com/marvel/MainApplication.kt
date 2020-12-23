package br.com.marvel

import android.app.Application
import br.com.marvel.module.AppModule
import br.com.marvel.module.NetworkModule
import br.com.marvel.module.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(AppModule, databaseModule, NetworkModule))
        }

    }
}