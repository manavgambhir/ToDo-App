package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.SimpleTimeZone


const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    var finalDate = 0L
    var finalTime = 0L

    lateinit var myCalender:Calendar            //Holds Value of Date Time Month Day
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private val labels = arrayListOf("Personal","Business","Insurance","Shopping","Banking")

//    val db by lazy {
//        Room.databaseBuilder(this,
//            AppDatabase::class.java,
//            DB_NAME
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//    }

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)

        setUpSpinner()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdt->{
                setDateListener()
            }

            R.id.timeEdt->{
                setTimeListener()
            }

            R.id.saveBtn->{
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        val title=etTitle.text.toString()
//        FinalTitle=title
        val description=etTask.text.toString()
        val category=spinnerCategory.selectedItem.toString()
//
        GlobalScope.launch(Dispatchers.Main){
            val id = withContext(Dispatchers.IO){
                return@withContext db.todoDao().insertTask(
                    TodoModel(
                        title,
                        description,
                        category,
                        finalDate,
                        finalTime
                    )
                )
            }
            finish()
        }
    }

    private fun setDateListener() {
        // Close the keyboard if it is open.
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager!!.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        myCalender = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener{ _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            myCalender.set(Calendar.YEAR,year)
            myCalender.set(Calendar.MONTH,month)
            myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalender.get(Calendar.YEAR),
            myCalender.get(Calendar.MONTH),
            myCalender.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        //Mon, 14 Aug 2023
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalender.time.time
        dateEdt.setText(sdf.format(myCalender.time))
        timeInpLayout.visibility = View.VISIBLE
    }



    private fun setTimeListener() {
        myCalender = Calendar.getInstance()
        timeSetListener = TimePickerDialog.OnTimeSetListener{ _: TimePicker, hourOfDay: Int, min: Int ->
            myCalender.set(Calendar.HOUR_OF_DAY,hourOfDay)
            myCalender.set(Calendar.MINUTE,min)
            updateTime()
        }

        val time = TimePickerDialog(
            this,
            timeSetListener,
            myCalender.get(Calendar.HOUR_OF_DAY),
            myCalender.get(Calendar.MINUTE),false
        )
        time.show()

    }

    private fun updateTime() {
        // 4:15 am
        val myFormat="h:mm a"
        val sdf = SimpleDateFormat(myFormat)
        finalTime = myCalender.time.time
        timeEdt.setText(sdf.format(myCalender.time))
    }



    private fun setUpSpinner() {
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,labels)

//        labels.sort()
        spinnerCategory.adapter = adapter
    }


}