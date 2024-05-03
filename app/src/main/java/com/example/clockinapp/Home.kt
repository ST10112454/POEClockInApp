package com.example.clockinapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewDebug.FlagToString
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clockinapp.model.UserData
import com.example.clockinapp.view.UserAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {

    private lateinit var addsBtn:FloatingActionButton
    private lateinit var recv:RecyclerView
    private lateinit var userList: ArrayList<UserData>
    private lateinit var userAdapter: UserAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        userList = ArrayList()
        addsBtn = findViewById(R.id.addingBtn)
        recv =findViewById(R.id.mRecycler)
        userAdapter = UserAdapter(this,userList)
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = userAdapter
        addsBtn.setOnClickListener{ addInfo()}

        //NAVIGATION
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_home

        bottomNavigationView.setOnItemSelectedListener {menuItem->
            when (menuItem.itemId){
                R.id.bottom_home ->
                    {true}
                R.id.bottom_EntryList ->{startActivity(Intent(applicationContext, EntryList::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_AddEntry ->{startActivity(Intent(applicationContext,NewEntry ::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true}
                R.id.bottom_Goal ->{startActivity(Intent(applicationContext, Report::class.java))
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    finish()
                    true}
                else -> false
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addInfo() {
        val inflter = LayoutInflater.from(this )
        val v = inflter.inflate(R.layout.add_item,null )
        val projectName = v.findViewById<EditText>(R.id.ProjectName)
        val description = v.findViewById<EditText>(R.id.Description )
        val date = v.findViewById<EditText>(R.id.Date )
        val start = v.findViewById<EditText>(R.id.Start )
        val end = v.findViewById<EditText>(R.id.End )
        val category = v.findViewById<EditText>(R.id.Category )

        val addDiaglog = AlertDialog.Builder(this)
        addDiaglog.setView(v)
        addDiaglog.setPositiveButton("Ok"){
            diaglog,_->
            val nameOfP = projectName.text.toString()
            val desc = description.text.toString()
            val date = date.text.toString()
            val start = start.text.toString()
            val end = end.text.toString()
            val category = category.text.toString()
            userList.add(UserData("Project: $projectName", "Description :$description" ))
            userAdapter.notifyDataSetChanged()
            Toast.makeText(this,"Adding Entry Infromation successful ",Toast.LENGTH_SHORT).show()
            diaglog.dismiss()
        }
        addDiaglog.setNegativeButton("Cancel"){
            dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
        }
        addDiaglog.create()
        addDiaglog.show()

    }
}