package ru.mamsikgames.smartkid.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.data.db.SmartDb
import ru.mamsikgames.smartkid.data.repository.SmartRepositoryImpl
import ru.mamsikgames.smartkid.data.repository.TaskRepositoryImpl
import ru.mamsikgames.smartkid.domain.SmartRepository
import ru.mamsikgames.smartkid.domain.TaskRepository
import ru.mamsikgames.smartkid.domain.impl.ChooseLevelInteractorImpl
import ru.mamsikgames.smartkid.domain.impl.GameInteractorImpl
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel
import ru.mamsikgames.smartkid.ui.viewmodel.GameViewModel

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), SmartDb::class.java, "smart_db")
            .createFromAsset("smart_db.db")
            .build()
    }
    single { get<SmartDb>().smartDao() }
}

val repositoryModule = module {
    factory<SmartRepository> { SmartRepositoryImpl(get()) }
    factory<TaskRepository> { TaskRepositoryImpl() }
    factory { GameSounds }
}

val interactorModule = module {
    factory<ChooseLevelInteractor> { ChooseLevelInteractorImpl(get()) }
    factory<GameInteractor> { GameInteractorImpl(get(), get()) }
}

val viewModelModule = module {
    viewModel { ChooseLevelViewModel(get()) }
    viewModel { GameViewModel(get()) }
}