package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import presentation.screen.HomeScreenViewModel

actual val viewModelModule = module {
    viewModelOf(::HomeScreenViewModel)
}