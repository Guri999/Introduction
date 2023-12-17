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

    val profileRefresh = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK ) dataLoad()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val swipe: SwipeRefreshLayout = findViewById(R.id.swipe)
        val quit = findViewById<Button>(R.id.btn_quit)
        val btnMbti = findViewById<Button>(R.id.btn_mbti)
        val editProfile = findViewById<ImageButton>(R.id.edit_profile)
        val id = intent.getStringExtra("id")

        dataLoad()

        swipe.setOnRefreshListener {
            dataLoad()
            swipe.isRefreshing = false
        }
        btnMbti.isEnabled = false
        editProfile.setOnClickListener {
            val intent = Intent(this, profileActivity::class.java)
            intent.putExtra("editId", id)
            profileRefresh.launch(intent)
        }

        quit.setOnClickListener {
            finish()
        }
    }

    fun dataLoad() {
        val userList: ArrayList<User> = UserList.userList
        val id = intent.getStringExtra("id")
        val name = userList.find { it.id == id }!!.name
        val email = userList.find { it.id == id }!!.email
        val age = userList.find { it.id == id }?.age
        val mbti = userList.find { it.id == id }?.mbti
        val intro = userList.find { it.id == id }?.introduce
        val introduce = findViewById<TextView>(R.id.introduce)
        val img = findViewById<ImageView>(R.id.random_img)
        val userEmail = findViewById<TextView>(R.id.user_email)
        val userId = findViewById<TextView>(R.id.user_id)
        val userName = findViewById<TextView>(R.id.user_name)
        val userAge = findViewById<TextView>(R.id.user_age)
        val userMBTI = findViewById<TextView>(R.id.user_mbti)
        val random = ThreadLocalRandom.current().nextInt(1, 6)

        when (random) {
            1 -> img.setImageResource(R.drawable.danger)
            2 -> img.setImageResource(R.drawable.dogsound)
            3 -> img.setImageResource(R.drawable.doolyswelcome)
            4 -> img.setImageResource(R.drawable.pikicast285983125)
            5 -> img.setImageResource(R.drawable.pxfuel)
        }

        userEmail.setText("${email}")
        userId.setText("ID: ${id}")
        userName.setText("이름: ${name}")
        userAge.setText("나이: ${age}")
        userMBTI.setText("MBTI: ${mbti}")
        introduce.setText("${intro}")
    }

}