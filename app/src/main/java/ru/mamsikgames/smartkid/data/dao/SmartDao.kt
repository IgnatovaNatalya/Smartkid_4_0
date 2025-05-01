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
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Round

@Dao
interface SmartDao {

//round
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertRound(r: RoundEntity): Single<Long>

    @Update
    fun updateRound(r: RoundEntity): Completable

    @Query("SELECT * FROM Round WHERE userId =:userId AND levelId= :levelId AND finished = 0 ORDER BY id DESC LIMIT 1")
    fun getPendingRound(userId:Int, levelId:Int): Maybe<Round>

//user
    @Update
    fun setCurrent(u: UserEntity): Completable

    @Query("UPDATE User set isCurrent=0")
    fun removeCurrent(): Completable

    @Query("UPDATE User set isCurrent=(userId =:userId)")
    fun setCurrentUser(userId:Int): Completable

    @Query("INSERT INTO User (userName, isCurrent) values(:strName,1)")
    fun addUser(strName:String): Completable

    @Query("SELECT * FROM User")
    fun getListUsers(): Flowable<List<UserEntity>>

    @Query("SELECT * FROM User WHERE isCurrent = 1")
    fun getCurrentUser(): Flowable<UserEntity>

 //leaders
    @Query("SELECT SUM(numCorrect)*100/SUM(numEfforts) AS rate FROM (select * from Round where finished = 1) GROUP BY userId HAVING userId=:userId ")
    fun getRate(userId:Int): Flowable<Int>

    @Query("SELECT userId, SUM(numCorrect)*100/SUM(numEfforts) AS rate ,SUM(numTasks) AS tasks , userId=:userId as myPlace FROM (select * FROM Round WHERE finished = 1) GROUP BY userId ORDER BY rate DESC , tasks DESC")
    fun getRates(userId: Int): Single<List<Rate>>

    @Query("SELECT userId, userName , SUM(numCorrect)*100/SUM(numEfforts) AS rate ,SUM(numTasks) AS tasks  " +
            "FROM (select Round.*, userName FROM Round inner join User on Round.userId = User.userId WHERE finished = 1 ) " +
            "GROUP BY userId ORDER BY rate DESC , tasks DESC")
    fun getLeaders(): Flowable<List<Leader>>


//level
    //@Query("SELECT id, name, codeName FROM Level ORDER BY ord")
    @Query("SELECT lg.name as groupName, l.id, l.name as name , l.codeName FROM LevelGroup lg INNER JOIN Level l ON lg.id = l.levelGroup ORDER BY l.ord")
    fun getListLevelsAndGroups(): Flowable<List<LevelModel>>

    @Query("SELECT COUNT(id) FROM Level")
    fun getCountLevels(): Flowable<Int>

    @Query("SELECT * FROM Level WHERE id =:id")
    fun getLevelParams(id:Int): Single<LevelParams>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertLevel(op: LevelEntity): Completable

//level group

    @Query("SELECT * FROM LevelGroup  ")
    fun getListLevelGroups(): Flowable<List<LevelGroupEntity>>
}