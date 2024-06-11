package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.screen.home.HomeScreenViewModel
import presentation.screen.splash.SplashScreenViewModel

actual val viewModelModule = module {
    factoryOf(::HomeScreenViewModel)
    factoryOf(::SplashScreenViewModel)
}