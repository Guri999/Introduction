package com.example.introduction

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import java.lang.StringBuilder

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emails = arrayOf("naver.com", "gmail.com", "nate.com", "직접입력")
        var userList = UserList.userList
        val inputName = findViewById<EditText>(R.id.input_name)
        val inputId = findViewById<EditText>(R.id.input_id)
        val inputPassword = findViewById<EditText>(R.id.input_pass)
        val inputEmailId = findViewById<EditText>(R.id.input_email_id)
        val inputEmail = findViewById<EditText>(R.id.input_email)
        val emailSpinner = findViewById<Spinner>(R.id.email_spinner)
        emailSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emails)
        val inputPasswordCheck = findViewById<EditText>(R.id.input_pass_check)
        val signBtn = findViewById<Button>(R.id.btn_sign)
        val emailRegex = Regex("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        val passText = findViewById<TextView>(R.id.pass_text)

        inputPassword.error = "10자리 이상,특수문자,대문자 포함"
        signBtn.isEnabled = false

        /*이메일 주소 스피너*/
        emailSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val email = emails[position]
                if (email != "직접입력") {
                    inputEmail.visibility = View.INVISIBLE
                    inputEmail.setText(email)
                    emailSpinner.visibility = View.VISIBLE
                } else {
                    inputEmail.visibility = View.VISIBLE
                    inputEmail.requestFocus()
                    inputEmail.setText(null)
                    inputEmail.setHint("email.com")
                    emailSpinner.visibility = View.INVISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                inputEmail.visibility = View.INVISIBLE
                inputEmail.setText("naver.com")
            }

        }

        inputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (inputEmail.text.isEmpty()) {
                    inputEmail.error = "올바른 이메일을 입력해주세요."
                    emailSpinner.visibility = View.VISIBLE
                    inputEmail.setHint(null)
                } else {
                    inputEmail.error = null
                    emailSpinner.visibility = View.INVISIBLE
                    inputEmail.setHint("email.com")
                }
            }
        }
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (inputName.text.isEmpty()) inputName.error = "이름을 입력해 주세요"
                else if (inputName.text.toString()
                        .matches(Regex(".*[!@#$%^&*()_+].*"))
                ) inputName.error = "이름에 특수문자를 쓸수 없습니다"
                else inputName.error = null

                if (inputId.text.isEmpty()) inputId.error = "id를 입력해주세요"
                else if (inputId.text.toString().length !in 2..8) inputId.error = "ID는 2~8글자여야 합니다"
                else if (inputId.text.toString()
                        .matches(Regex(".*[!@#$%^&*()_+].*"))
                ) inputId.error = "ID에 특수문자를 쓸수 없습니다"
                else if (userList.any { it.id == inputId.text.toString() }) inputId.error =
                    "이미 사용 중인 ID입니다"
                else inputId.error = null

                if (inputEmailId.text.isEmpty()) inputEmailId.error = "id를 입력해주세요"
                else if (inputEmailId.text.toString()
                        .matches(Regex(".*[!@#$%^&*()_+].*"))
                ) inputName.error = "ID에 특수문자를 쓸수 없습니다"
                else inputEmailId.error = null

                if (inputEmail.text.isEmpty()) {
                    inputEmail.error = "메일을 입력해주세요"
                } else if (inputEmail.text.toString().matches(emailRegex)) inputEmail.error = null
                else inputEmail.error = "올바른 이메일 형식이 아닙니다"

                if (inputPassword.text.isEmpty()) passText.visibility = View.VISIBLE
                else passText.visibility = View.INVISIBLE

                if (inputPassword.text.toString().length !in 10..16) inputPassword.error =
                    "10~16자여야 합니다"
                else if (inputPassword.text.toString()
                        .matches(Regex(".*[!@#$%^&*()_+].*")) == false
                ) inputPassword.error = "특수문자가 하나 이상 들어가야합니다"
                else if (inputPassword.text.toString()
                        .find { it in 'A'..'Z' } == null
                ) inputPassword.error = "대소문자가 하나 이상 들어가야합니다"

                if (inputPasswordCheck.text.toString() != inputPassword.text.toString()) inputPasswordCheck.error =
                    "비밀번호와 다릅니다"
                else inputPasswordCheck.error = null

                signBtn.isEnabled =
                    inputName.error == null && inputId.error == null && inputEmailId.error == null && inputPassword.error == null && inputPasswordCheck.error == null
            }
        }
        inputName.addTextChangedListener(watcher)
        inputId.addTextChangedListener(watcher)
        inputPassword.addTextChangedListener(watcher)
        inputEmail.addTextChangedListener(watcher)
        inputPasswordCheck.addTextChangedListener(watcher)

        signBtn.setOnClickListener {
            val name = inputName.getText().toString()
            val id = inputId.getText().toString()
            val email = StringBuilder()
            email.append(inputEmailId.text)
            email.append("@")
            email.append(inputEmail.text)
            val password = inputPassword.getText().toString()

            val user = User(name, id, password, email.toString(), null, null, "", null)
            val intent = Intent(this, SignInActivity::class.java)
            UserList.userList.add(user)
            val result = intent.apply {
                putExtra("id", id)
                putExtra("password", password)
            }
            setResult(RESULT_OK,result)
            finish()
        }
    }

}
