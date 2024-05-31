package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.screen.home.HomeScreenViewModel

actual val viewModelModule = module {
    singleOf(::HomeScreenViewModel)
}