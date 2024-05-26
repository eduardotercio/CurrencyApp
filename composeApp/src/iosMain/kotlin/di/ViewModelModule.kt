package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.screen.HomeScreenViewModel

actual val viewModelModule = module {
    singleOf(::HomeScreenViewModel)
}