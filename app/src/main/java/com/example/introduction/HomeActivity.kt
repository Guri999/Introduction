package com.example.introduction

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.introduction.R
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private val profileRefresh =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) init()
        }

    private val quit: Button by lazy {
        findViewById(R.id.btn_quit)
    }

    private val editProfile: ImageButton by lazy {
        findViewById(R.id.edit_profile)
    }

    private val id: String? by lazy {
        intent.getStringExtra("id")
    }

    private val userList: ArrayList<User> by lazy {
        UserList.userList
    }


    private val email: String by lazy {
        userList.find { it.id == id }!!.email
    }

    private lateinit var name: String

    private var age: Int? = null

    private var mbti: String? = null

    private var intro: String? = null

    private val introduce: TextView by lazy {
        findViewById(R.id.introduce)
    }

    private val img: ImageView by lazy {
        findViewById(R.id.random_img)
    }

    private val userEmail: TextView by lazy {
        findViewById(R.id.user_email)
    }

    private val userId: TextView by lazy {
        findViewById(R.id.user_id)
    }

    private val userName: TextView by lazy {
        findViewById(R.id.user_name)
    }

    private val userAge: TextView by lazy {
        findViewById(R.id.user_age)
    }

    private val userMBTI: TextView by lazy {
        findViewById(R.id.user_mbti)
    }

    private val random: Int by lazy {
        ThreadLocalRandom.current().nextInt(1, 6)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init() {
        setUserData()

        setRandomImg()

        setHomeButton()
    }

    private fun setUserData() {
        name = userList.find { it.id == id }!!.name
        age = userList.find { it.id == id }?.age
        mbti = userList.find { it.id == id }?.mbti
        intro = userList.find { it.id == id }?.introduce

        userEmail.setText("${email}")
        userId.setText("ID: ${id}")
        userName.setText("이름: ${name}")
        userAge.setText("나이: ${age}")
        userMBTI.setText("MBTI: ${mbti}")
        introduce.setText("${intro}")
    }

    private fun setHomeButton() {
        editProfile.setOnClickListener {
            val intent = Intent(this, profileActivity::class.java)
            intent.putExtra("editId", id)
            profileRefresh.launch(intent)
        }

        quit.setOnClickListener {
            finish()
        }
    }

    private fun setRandomImg() {

        when (random) {
            1 -> img.setImageResource(R.drawable.danger)
            2 -> img.setImageResource(R.drawable.dogsound)
            3 -> img.setImageResource(R.drawable.doolyswelcome)
            4 -> img.setImageResource(R.drawable.pikicast285983125)
            5 -> img.setImageResource(R.drawable.pxfuel)
        }
    }


}