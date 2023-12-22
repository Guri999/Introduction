package com.example.introduction

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.introduction.R
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private val profileRefresh =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) init()
        }

    private val quit: ConstraintLayout by lazy {
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
        userId.setText("${getString(R.string.home_id)} ${id}")
        userName.setText("${getString(R.string.home_name)} ${name}")
        userAge.setText("${getString(R.string.home_age)} ${age}")
        userMBTI.setText("${getString(R.string.home_mbti)} ${mbti}")
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

        val imgId = when ((1..5).random()) {
            1 -> R.drawable.danger
            2 -> R.drawable.dogsound
            3 -> R.drawable.doolyswelcome
            4 -> R.drawable.pikicast285983125
            5 -> R.drawable.pxfuel
            else -> R.drawable.danger
        }

        img.setImageDrawable(ResourcesCompat.getDrawable(resources, imgId, null))
    }


}