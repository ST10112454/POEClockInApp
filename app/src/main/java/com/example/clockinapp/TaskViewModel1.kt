package com.example.clockinapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TaskViewModel1 : ViewModel() {

    var taskItems1 = MutableLiveData<MutableList<TaskItem1>>()
    init {
        taskItems1.value = mutableListOf()
    }
    fun addTaskItem(newTask1: TaskItem1)
    {
        val list = taskItems1.value
        list!!.add(newTask1)
        taskItems1.postValue(list)
    }
    fun updateTaskItem(id: UUID, name: String, dueTime: LocalTime?)
    {
        val list = taskItems1.value
        val task = list!!.find { it.id == id }!!
        task.name = name
        task.dueTime = dueTime
        taskItems1.postValue(list)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setCompleted(taskItem1: TaskItem1)
    {
        val list = taskItems1.value
        val task = list!!.find { it.id == taskItem1.id }!!
        if (task.completedDate == null)
            task.completedDate = LocalDate.now()
        taskItems1.postValue(list)
    }


}