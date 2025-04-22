package ru.mamsikgames.smartkid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.db.SmartDb
import ru.mamsikgames.smartkid.data.db.entity.Operation
import ru.mamsikgames.smartkid.data.db.entity.Round
import ru.mamsikgames.smartkid.data.db.entity.User
import ru.mamsikgames.smartkid.data.repository.SmartRepository
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName


class SmartViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private val _recordUsers = MutableLiveData<List<User>>()
    var recordUsers: LiveData<List<User>> = _recordUsers

    private val _recordCurUser = MutableLiveData<User>()
    var recordCurUser: LiveData<User> = _recordCurUser

    private val _recordCountUsers = MutableLiveData<Int>()
    var recordCountUsers: LiveData<Int> = _recordCountUsers

    private val _recordRounds = MutableLiveData<List<RoundWithName>>()
    var recordRounds: LiveData<List<RoundWithName>> = _recordRounds

    private val _recordPendingRound = MutableLiveData<Round>()
    var recordPendingRound:LiveData<Round> = _recordPendingRound

    private val _recordRates = MutableLiveData<List<Rate>>()
    var recordRates:LiveData<List<Rate>> = _recordRates

    private val _recordLeaders = MutableLiveData<List<Leader>>()
    var recordLeaders:LiveData<List<Leader>> = _recordLeaders

    private val _recordNewRound = MutableLiveData<Long>()
    var recordNewRound:LiveData<Long> = _recordNewRound

    private val _recordOperations = MutableLiveData<List<Operation>>()
    var recordOperations:LiveData<List<Operation>> = _recordOperations


    private var smartRepository: SmartRepository =
        SmartRepository(SmartDb.getInstance(getApplication()).smartDao())


    fun addUser(strName:String) {
        smartRepository.addUser(strName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun insertRound(r:Round) {
        smartRepository.insertRound(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordNewRound.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun updateRound(r:Round) {
        smartRepository.updateRound(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun insertOperation(op:Operation) {
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

    fun setCurrentUser(userId:Int) {
        smartRepository.setCurrentUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getCurrentUser()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListRoundsWithNames(userId:Int) {
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

    fun getPendingRound(userId:Int, operationId:Int) {
        smartRepository.getPendingRound(userId, operationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordPendingRound.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getRates(userId:Int) {
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

    fun getCurrentUser() {
        smartRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordCurUser.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListOperations() {
        smartRepository.getListOperations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordOperations.postValue(it)
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
