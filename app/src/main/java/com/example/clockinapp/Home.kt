package com.example.clockinapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug.FlagToString
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
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
import java.time.DayOfWeek
import java.util.Calendar

class Home : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var addsBtn:FloatingActionButton
    private lateinit var recv:RecyclerView
    private lateinit var userList: ArrayList<UserData>
    private lateinit var userAdapter: UserAdapter
    private lateinit var imageView: ImageView
    private lateinit var button: FloatingActionButton


    var day =0
    var month =0
    var year =0
    var hour =0
    var minute =0

    var saveDay =0
    var saveMonth =0
    var saveYear =0
    var saveHour =0
    var saveMinute =0



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        //Home page

// ------------------------date picker
        pickDate()
// ------------------------Pick Image

    /*
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))


        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.floatingActionButton)
        button.setOnClickListener{
        ImagePicker.with(this)
        .crop()	    			//Crop image(Optional), Check Customization for more option
        .compress(1024)			//Final image size will be less than 1 MB(Optional)
        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
        .start()

        }
        */

        //set list
        userList = ArrayList()
        //set find id
        addsBtn = findViewById(R.id.addingBtn)
        recv =findViewById(R.id.mRecycler)
        //set Adapter
        userAdapter = UserAdapter(this,userList)
        //setRecycler view adapter
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = userAdapter
        //set dialog
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

    private fun pickDate() {

        //btn_timePicker.setOnClickListener{}
    }

    private fun addInfo() {
        val inflter = LayoutInflater.from(this )
        val v = inflter.inflate(R.layout.add_item,null )
        //set view
        val projectName = v.findViewById<EditText>(R.id.ProjectName)
        val description = v.findViewById<EditText>(R.id.Description )


        val addDiaglog = AlertDialog.Builder(this)
        addDiaglog.setView(v)
        addDiaglog.setPositiveButton("Ok"){
            diaglog,_->
            val nameOfP = projectName.text.toString()
            val desc = description.text.toString()
            userList.add(UserData("Project: $nameOfP", "Description :$desc" ))
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
    private fun getDateTimeCalander(){
        val cal = Calendar.getInstance()
        day  = cal.get(Calendar.DAY_OF_MONTH)
        month  = cal.get(Calendar.MONTH)
        year  = cal.get(Calendar.YEAR)
        hour  = cal.get(Calendar.HOUR)
        minute  = cal.get(Calendar.MINUTE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageView.setImageURI(data?.data)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }
}