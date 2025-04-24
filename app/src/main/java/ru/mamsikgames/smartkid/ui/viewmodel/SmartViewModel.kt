package ru.mamsikgames.smartkid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.SmartRepository
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName
import org.koin.core.component.KoinComponent


class SmartViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val compositeDisposable = CompositeDisposable()

    private val _recordUsers = MutableLiveData<List<UserEntity>>()
    var recordUsers: LiveData<List<UserEntity>> = _recordUsers

    private val _recordCurUser = MutableLiveData<UserEntity>()
    var recordCurUser: LiveData<UserEntity> = _recordCurUser

    private val _recordCountUsers = MutableLiveData<Int>()
    var recordCountUsers: LiveData<Int> = _recordCountUsers

    private val _recordRounds = MutableLiveData<List<RoundWithName>>()
    var recordRounds: LiveData<List<RoundWithName>> = _recordRounds


    private val _recordRates = MutableLiveData<List<Rate>>()
    var recordRates: LiveData<List<Rate>> = _recordRates

    private val _recordLeaders = MutableLiveData<List<Leader>>()
    var recordLeaders: LiveData<List<Leader>> = _recordLeaders



    private val _recordOperations = MutableLiveData<List<LevelEntity>>()
    var recordOperations: LiveData<List<LevelEntity>> = _recordOperations

    private val smartRepository: SmartRepository = getKoin().get()

    fun addUser(strName: String) {
        smartRepository.addUser(strName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }


    fun insertOperation(op: LevelEntity) {
        smartRepository.insertOperation(op)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //_recordOperations.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

//    fun setCurrentUser(userId:Int) {
//        smartRepository.setCurrentUser(userId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                getCurrentUser()
//            }, {
//            }).let {
//                compositeDisposable.add(it)
//            }
//    }

    fun getListRoundsWithNames(userId: Int) {
        smartRepository.getListRoundsWithNames(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordRounds.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }


    fun getRates(userId: Int) {
        smartRepository.getRates(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordRates.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getLeaders() {
        smartRepository.getLeaders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordLeaders.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListUsers() {
        smartRepository.getListUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordUsers.postValue(it)
                _recordCountUsers.postValue(it.size)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
