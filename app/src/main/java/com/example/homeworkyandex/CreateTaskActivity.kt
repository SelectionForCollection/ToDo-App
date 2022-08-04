package com.example.homeworkyandex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreateTaskActivity : AppCompatActivity() {

    private var idCounter: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task_activity)

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            MainActivity().addToDataSet(Task(idCounter, findViewById<EditText>(R.id.edit_text).text.toString(), false))
        }
    }
}