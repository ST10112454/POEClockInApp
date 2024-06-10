package com.example.clockinapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.E

class Graph : AppCompatActivity() {

    lateinit var pieChart: PieChart
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        //navigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_EntryList

        bottomNavigationView.setOnItemSelectedListener {menuItem->
            when (menuItem.itemId){
                R.id.bottom_home ->{startActivity(Intent(applicationContext, Home::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_EntryList ->
                {startActivity(Intent(applicationContext, EntryList::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_AddEntry ->{
                    true}
                R.id.bottom_Goal ->{startActivity(Intent(applicationContext, Report::class.java))
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    finish()
                    true}
                else -> false
            }

        }

        pieChart = findViewById(R.id.pie_chart)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            fetchGoals(userId)
        }
    }

    private fun fetchGoals(userId: String) {
        val goalsQuery = database.child("goals").orderByChild("userId").equalTo(userId)
        val tasksQuery = database.child("taskentry").orderByChild("userId").equalTo(userId)

        goalsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(goalsSnapshot: DataSnapshot) {
                for (goalSnapshot in goalsSnapshot.children) {
                    val minDailyGoal = goalSnapshot.child("minDailyGoal").getValue(Int::class.java) ?: 0
                    val maxDailyGoal = goalSnapshot.child("maxDailyGoal").getValue(Int::class.java) ?: 0

                    tasksQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(tasksSnapshot: DataSnapshot) {
                            var totalHoursWorked = 0f

                            for (taskSnapshot in tasksSnapshot.children) {
                                val startTime = taskSnapshot.child("startTime").getValue(Int::class.java) ?: 0
                                val endTime = taskSnapshot.child("endTime").getValue(Int::class.java) ?: 0
                                totalHoursWorked += (endTime - startTime).toFloat()
                            }

                            updatePieChart(minDailyGoal.toFloat(), maxDailyGoal.toFloat(), totalHoursWorked)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e("Graph", "Database error: ${databaseError.message}")
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Graph", "Database error: ${databaseError.message}")
            }
        })
    }

    private fun updatePieChart(minGoal: Float, maxGoal: Float, totalHours: Float) {
        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(minGoal, "Min Goal"))
        list.add(PieEntry(maxGoal, "Max Goal"))
        list.add(PieEntry(totalHours, "Hours Worked"))

        val pieDataSet = PieDataSet(list, "Goals & Hours Worked")

        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS, 255)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 15f

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.text = " "
        pieChart.centerText = "Goals & Hours Worked"
        pieChart.animateY(2000)
    }

}