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
    private val mbtiList: ArrayList<String> by lazy {
        arrayListOf(
            "ENFJ", "ENFP", "ENTJ", "ENTP",
            "ESFJ", "ESFP", "ESTJ", "ESTP",
            "INFJ", "INFP", "INTJ", "INTP",
            "ISFJ", "ISFP", "ISTJ", "ISTP"
        )
    }

    private val yearList: ArrayList<String> by lazy {
        (1950..2023).toList().map { it.toString() } as ArrayList<String>
    }

    private val monthList: ArrayList<String> by lazy {
        (1..12).toList().map { it.toString() } as ArrayList<String>
    }

    private val dateList: ArrayList<String> by lazy {
        (1..31).toList().map { it.toString() } as ArrayList<String>
    }

    private val userList: ArrayList<User> by lazy {
        UserList.userList
    }

    private val id: String by lazy {
        intent.getStringExtra("editId").toString()
    }

    private val inputName: EditText by lazy {
        findViewById(R.id.input_name)
    }
    private val inputMbti: Spinner by lazy {
        findViewById(R.id.input_mbti)
    }
    private val inputIntro: EditText by lazy {
        findViewById(R.id.input_introduce)
    }
    private val npYear: NumberPicker by lazy {
        findViewById(R.id.npYear)
    }
    private val npMonth: NumberPicker by lazy {
        findViewById(R.id.npMonth)
    }
    private val npDay: NumberPicker by lazy {
        findViewById(R.id.npDay)
    }
    private val btnBack: Button by lazy {
        findViewById(R.id.btn_back)
    }

    private val btnEdit: Button by lazy {
        findViewById<Button>(R.id.btn_edit)
    }
    private lateinit var name: String
    private lateinit var birth: String
    private lateinit var mbti: String
    private lateinit var intro: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
    }

    private fun init() {
        setSaveUserData()

        setEditButton()
    }

    private fun setSaveUserData() {
        userList
        name = userList.find { it.id == id }?.name.toString()
        birth = userList.find { it.id == id }?.birth.toString()
        mbti = userList.find { it.id == id }?.mbti.toString()
        intro = userList.find { it.id == id }?.introduce.toString()

        inputName.setText("${name}")
        inputIntro.setText("${intro}")

        //NumberPicker
        setBirthProvider()

        //MBTI Spinner
        setItemSelectedListener()
    }

    private fun setBirthProvider() {
        val npRange = birth?.split("-")

        npYear.run {
            minValue = 0
            maxValue = yearList.size - 1
            wrapSelectorWheel = false
            displayedValues = yearList.toTypedArray()
            value = yearList.indexOf(npRange?.getOrNull(0) ?: "1980")
        }
        npMonth.run {
            minValue = 0
            maxValue = monthList.size - 1
            displayedValues = monthList.toTypedArray()
            value = monthList.indexOf(npRange?.getOrNull(1) ?: "0")
        }
        npDay.run {
            minValue = 0
            maxValue = dateList.size - 1
            displayedValues = dateList.toTypedArray()
            value = dateList.indexOf(npRange?.getOrNull(2) ?: "0")
        }
    }

    private fun setItemSelectedListener() {
        inputMbti.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mbtiList)
        inputMbti.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mbti = mbtiList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun setEditButton() {
        btnEdit.setOnClickListener {
            UserList.userList.find { it.id == id }?.let {
                it.name = inputName.text.toString()
                it.age = LocalDate.now().year - yearList[npYear.value].toInt() + 1
                it.introduce = inputIntro.text.toString()
                it.mbti = mbti
                it.birth =
                    "${yearList[npYear.value]}-${monthList[npMonth.value]}-${dateList[npDay.value]}"
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