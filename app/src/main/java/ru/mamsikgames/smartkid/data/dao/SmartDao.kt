package ru.mamsikgames.smartkid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName
import ru.mamsikgames.smartkid.data.entity.OperationEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity

@Dao
interface SmartDao {

    @Query("INSERT INTO User (userName, isCurrent) values(:strName,1)")
    fun addUser(strName:String): Completable

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertRound(r: RoundEntity): Single<Long>

    @Update
    fun updateRound(r: RoundEntity): Completable

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertOperation(op: OperationEntity): Completable

    @Update
    fun setCurrent(u: UserEntity): Completable

    @Query("UPDATE User set isCurrent=0")
    fun removeCurrent(): Completable

    @Query("UPDATE User set isCurrent=(userId =:userId)")
    fun setCurrentUser(userId:Int): Completable

    @Query("SELECT Round.*, Operation.codeName FROM Round INNER JOIN Operation ON Round.operationId = Operation.id WHERE Round.userId=:userId ORDER BY Round.roundEnd DESC")
    fun getListRoundsWithNames(userId:Int): Flowable<List<RoundWithName>>

    @Query("SELECT * FROM Round WHERE userId =:userId AND operationId= :operationId AND finished = 0 ORDER BY id DESC LIMIT 1")
    fun getPendingRound(userId:Int, operationId:Int): Maybe<RoundEntity>

    @Query("SELECT SUM(numCorrect)*100/SUM(numEfforts) AS rate FROM (select * from Round where finished = 1) GROUP BY userId HAVING userId=:userId ")
    fun getRate(userId:Int): Flowable<Int>

    @Query("SELECT userId, SUM(numCorrect)*100/SUM(numEfforts) AS rate ,SUM(numTasks) AS tasks , userId=:userId as myPlace FROM (select * FROM Round WHERE finished = 1) GROUP BY userId ORDER BY rate DESC , tasks DESC")
    fun getRates(userId: Int): Single<List<Rate>>

    @Query("SELECT userId, userName , SUM(numCorrect)*100/SUM(numEfforts) AS rate ,SUM(numTasks) AS tasks  " +
            "FROM (select Round.*, userName FROM Round inner join User on Round.userId = User.userId WHERE finished = 1 ) " +
            "GROUP BY userId ORDER BY rate DESC , tasks DESC")
    fun getLeaders(): Flowable<List<Leader>>

    @Query("SELECT * FROM User")
    fun getListUsers(): Flowable<List<UserEntity>>

    @Query("SELECT * FROM User where isCurrent=1")
    fun getCurrentUser(): Flowable<UserEntity>

    @Query("SELECT * FROM Operation ORDER BY ord")
    fun getListOperations(): Flowable<List<OperationEntity>>

    @Query("SELECT COUNT(id) FROM Operation")
    fun getCountOperations(): Flowable<Int>

    @Query("SELECT * FROM Operation WHERE id =:id")
    fun getOperation(id:Int): Single<OperationEntity>
}