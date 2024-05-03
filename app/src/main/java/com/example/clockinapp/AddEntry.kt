package com.example.clockinapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clockinapp.databinding.FragmentAddEntryBinding
import com.example.clockinapp.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddEntry : AppCompatActivity() , TaskItemClickListener {
    //private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var binding1: FragmentAddEntryBinding
    private lateinit var taskViewModel1: TaskViewModel1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_add_entry)

        //binding data
        binding1 = FragmentAddEntryBinding.inflate(layoutInflater)
        setContentView(binding1.root)
        taskViewModel1 = ViewModelProvider(this).get(TaskViewModel1::class.java)
        binding1.newTaskButton1.setOnClickListener{
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }



}

    override fun editTaskItem(taskItem: TaskItem) {
        TODO("Not yet implemented")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        TODO("Not yet implemented")
    }


}
