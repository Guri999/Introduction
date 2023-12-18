package com.example.introduction

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {


    private lateinit var idChk: EditText
    private lateinit var pasChk: EditText
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val id = result.data?.getStringExtra("id")
            val password = result.data?.getStringExtra("password")

            idChk.setText(id)
            pasChk.setText(password)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        idChk = findViewById(R.id.input_id)
        pasChk = findViewById(R.id.input_pass)
        val signInButton = findViewById<Button>(R.id.btn_sign_in)
        val signUpButton = findViewById<Button>(R.id.btn_create_account)
        //테스트용 아이디 입니다
        UserList.userList.add(User("a","a","a","a","",0,"ㅎㅇ","1980-01-01"))

        signInButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)


            if (UserList.userList.none { it.id == idChk.text.toString() }) Toast.makeText(this,"가입되지 않은 사용자입니다.",Toast.LENGTH_SHORT).show()
            else if (UserList.userList.any { it.id == idChk.text.toString() && it.password == pasChk.text.toString()  }) {
                intent.putExtra("id", idChk.text.toString())
                startActivity(intent)
            }
            else Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다",Toast.LENGTH_SHORT).show()
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            getContent.launch(intent)
        }
    }


}
