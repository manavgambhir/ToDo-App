package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao{
    @Insert
    suspend fun insertTask(todoModel: TodoModel): Long

    @Query ("SELECT * FROM TodoModel WHERE isFinished == 0")
    fun getTask(): LiveData<List<TodoModel>>

    @Query("UPDATE TodoModel Set isFinished=-1 where id=:uid")
    fun finishedTask(uid: Long)

    @Query("DELETE from TodoModel WHERE id=:uid")
    fun deleteTask(uid: Long)

}