package com.example.introduction

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import java.time.LocalDate

class profileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val yearList = (1950..2023).toList().map { it.toString() }
        val monthList = (1..12).toList().map { it.toString() }
        val dateList = (1..31).toList().map { it.toString() }

        val mbtiList = arrayListOf(
            "ENFJ", "ENFP", "ENTJ", "ENTP",
            "ESFJ", "ESFP", "ESTJ", "ESTP",
            "INFJ", "INFP", "INTJ", "INTP",
            "ISFJ", "ISFP", "ISTJ", "ISTP"
        )

        val userList: ArrayList<User> = UserList.userList
        val id = intent.getStringExtra("editId")
        var name = userList.find { it.id == id }?.name
        var birth = userList.find { it.id == id }?.birth
        var mbti = userList.find { it.id == id }?.mbti
        var intro = userList.find { it.id == id }?.introduce
        val inputName = findViewById<EditText>(R.id.input_name)
        val inputMbti = findViewById<Spinner>(R.id.input_mbti)
        inputMbti.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, mbtiList)
        val inputIntro = findViewById<EditText>(R.id.input_introduce)
        val npYear = findViewById<NumberPicker>(R.id.npYear)
        val npMonth = findViewById<NumberPicker>(R.id.npMonth)
        val npDay = findViewById<NumberPicker>(R.id.npDay)
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnEdit = findViewById<Button>(R.id.btn_edit)
        inputName.setText("${name}")
        inputIntro.setText("${intro}")

        val npRange = birth?.split("-")
        npYear.run {
            minValue = 0
            maxValue = yearList.size - 1
            wrapSelectorWheel = false
            displayedValues = yearList.toTypedArray()
            value = yearList.indexOf(npRange?.get(0) ?: "1950")
        }
        npMonth.run {
            minValue = 0
            maxValue = monthList.size - 1
            displayedValues = monthList.toTypedArray()
            value = yearList.indexOf(npRange?.get(1) ?: "1")
        }
        npDay.run {
            minValue = 0
            maxValue = dateList.size - 1
            displayedValues = dateList.toTypedArray()
            value = yearList.indexOf(npRange?.get(2) ?: "1")
        }

        inputMbti.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mbti = mbtiList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (mbti != null) inputMbti.setSelection(mbtiList.indexOf(mbti))
                else inputMbti.setSelection(0)
            }

        }

        btnEdit.setOnClickListener {

            UserList.userList.find { it.id == id }?.let {
                it.name = inputName.text.toString()
                it.age = LocalDate.now().year - yearList[npYear.value].toInt() + 1
                it.introduce = inputIntro.text.toString()
                it.mbti = mbti
                it.birth = "${yearList[npYear.value]}-${monthList[npMonth.value]}-${dateList[npDay.value]}"
            }
            val intent = Intent(this, HomeActivity::class.java)
            setResult(Activity.RESULT_OK, null)
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}