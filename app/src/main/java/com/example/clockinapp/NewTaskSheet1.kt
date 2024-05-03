package com.example.clockinapp

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.clockinapp.databinding.FragmentNewTaskSheet1Binding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NewTaskSheet1(var taskItem1: TaskItem1?)  : BottomSheetDialogFragment() {

    //private lateinit var binding: ActivityReportBinding
    private lateinit var binding1: FragmentNewTaskSheet1Binding
    private lateinit var taskViewModel1: TaskViewModel1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (taskItem1 != null)
        {
            binding1.taskTitle1.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding1.taskname1.text = editable.newEditable(taskItem1!!.name)
        }
        else
        {
            binding1.taskTitle1.text = "New Task"
        }
        taskViewModel1 = ViewModelProvider(activity).get(TaskViewModel1::class.java)
        binding1.saveButton1.setOnClickListener {
            saveAction()
        }
    }

    private fun saveAction() {
        val name = binding1.taskname1.text.toString()
        if(taskItem1 == null)
        {
            val newTask1 = TaskItem1(name,dueTime = null,null)
            taskViewModel1.addTaskItem(newTask1)
        }
        else
        {
            taskViewModel1.updateTaskItem(taskItem1!!.id, name, dueTime = null )
        }
        binding1.taskname1.setText("")
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding1 = FragmentNewTaskSheet1Binding.inflate(inflater,container,false)

        return binding1.root
    }
}