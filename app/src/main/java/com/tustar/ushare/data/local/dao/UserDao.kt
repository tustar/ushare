package com.tustar.ushare.data.local.dao

import androidx.room.*
import com.tustar.ushare.data.entry.User
import io.reactivex.Flowable


@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Flowable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)

    @Update
    fun updateUsers(vararg users: User)

    @Delete
    fun deleteUsers(vararg users: User)
}