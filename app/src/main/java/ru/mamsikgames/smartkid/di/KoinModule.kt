package ru.mamsikgames.smartkid.di

import org.koin.dsl.module
import ru.mamsikgames.smartkid.data.db.SmartDb
import ru.mamsikgames.smartkid.data.repository.SmartRepositoryImpl
import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import ru.mamsikgames.smartkid.domain.impl.ChooseLevelInteractorImpl
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel

val DatabaseModule = module {
    single {
        Room.databaseBuilder(get(), SmartDb::class.java, "smart_db")
            //.createFromAsset("smart_db.db")
            .build()
    }
    single { get<SmartDb>().smartDao() }
}

val repositoryModule = module {
    single<SmartRepositoryImpl> { SmartRepositoryImpl(get()) }
}

val interactorModule = module {
    factory<ChooseLevelInteractor> { ChooseLevelInteractorImpl(get()) }
}

val viewModelModule = module {
    viewModel { ChooseLevelViewModel(get()) }
}