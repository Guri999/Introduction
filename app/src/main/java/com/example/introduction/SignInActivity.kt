package com.example.introduction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.introduction.signup.SignUpActivity
import com.example.introduction.user.HomeActivity

class SignInActivity : AppCompatActivity() {

    private val signInButton: Button by lazy {
        findViewById(R.id.btn_sign_in)
    }

    private val signUpButton: Button by lazy {
        findViewById(R.id.btn_create_account)
    }

    private lateinit var idChk: EditText
    private lateinit var pasChk: EditText
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun init() {
        setSigUpUserData()

        setSignInButton()

        setSignUpButton()
    }

    private fun setSigUpUserData() {
        idChk = findViewById(R.id.input_id)
        pasChk = findViewById(R.id.input_pass)
    }

    private fun setSignInButton() {
        signInButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            if (UserList.userList.none { it.id == idChk.text.toString() }){
                Toast.makeText(this, getString(R.string.no_one_user_list), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (UserList.userList.any { it.id == idChk.text.toString() && it.password == pasChk.text.toString() }) {
                intent.putExtra("id", idChk.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.check_id_password_plz),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }

    private fun setSignUpButton() {
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            getContent.launch(intent)
        }
    }


}
