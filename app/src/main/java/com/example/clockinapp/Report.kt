package com.example.clockinapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clockinapp.databinding.ActivityReportBinding
import com.example.clockinapp.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class Report : AppCompatActivity() , TaskItemClickListener {
    //private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var binding: ActivityReportBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    //
    private var selectedDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_report)

        //binding data
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.newTaskButton.setOnClickListener{
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }
        binding.saveGoalsButton.setOnClickListener {
            saveGoals()
        }

        setRecyclerView()
        loadGoals()
        // Date picker button
        val datePickerButton: ImageButton = findViewById(R.id.datePickerButton)
        datePickerButton.setOnClickListener { showDatePickerDialog() }
        //Navigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_Goal

        bottomNavigationView.setOnItemSelectedListener {menuItem->
            when (menuItem.itemId){
                R.id.bottom_home ->
                {startActivity(Intent(applicationContext, Home::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_EntryList ->{startActivity(Intent(applicationContext, EntryList::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_AddEntry ->{startActivity(Intent(applicationContext, Graph::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_Goal ->{true}
                else -> false
            }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
             selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            findViewById<TextView>(R.id.selectedDate).text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun loadGoals() {
        val userId = auth.currentUser?.uid ?: return
        //val date = selectedDate ?: return
        database.reference.child("goals").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val minDailyGoal = snapshot.child("minDailyGoal").getValue(Double::class.java)
                val maxDailyGoal = snapshot.child("maxDailyGoal").getValue(Double::class.java)

                if (minDailyGoal != null && maxDailyGoal != null) {
                    findViewById<EditText>(R.id.minDailyGoal).setText(minDailyGoal.toString())
                    findViewById<EditText>(R.id.maxDailyGoal).setText(maxDailyGoal.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load goals", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveGoals() {
        val minDailyGoal = findViewById<EditText>(R.id.minDailyGoal).text.toString()
        val maxDailyGoal = findViewById<EditText>(R.id.maxDailyGoal).text.toString()

        if (minDailyGoal.isEmpty() || maxDailyGoal.isEmpty() || selectedDate.isNullOrEmpty()) {
            Toast.makeText(this, "Please fill both goals", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val goalId = database.reference.child("goals").push().key ?: return
        val goals = mapOf(
            "userId" to userId,
            "minDailyGoal" to minDailyGoal.toDouble(),
            "maxDailyGoal" to maxDailyGoal.toDouble(),
            "date" to selectedDate
        )

        database.reference.child("goals").child(goalId).setValue(goals)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Goals saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to save goals", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it,mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.setCompleted(taskItem)
    }
}