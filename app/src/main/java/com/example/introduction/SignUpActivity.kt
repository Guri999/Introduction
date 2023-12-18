package com.example.introduction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import kotlin.text.StringBuilder

class SignUpActivity : AppCompatActivity() {

    private lateinit var userList: ArrayList<User>

    private val inputName: EditText by lazy {
        findViewById<EditText>(R.id.input_name)
    }

    private val inputId: EditText by lazy {
        findViewById(R.id.input_id)
    }
    private val inputPassword: EditText by lazy {
        findViewById(R.id.input_pass)
    }
    private val inputEmailId: EditText by lazy {
        findViewById(R.id.input_email_id)
    }
    private val inputEmail: EditText by lazy {
        findViewById(R.id.input_email)
    }
    private val emailSpinner: Spinner by lazy {
        findViewById(R.id.email_spinner)
    }
    private val inputPasswordCheck: EditText by lazy {
        findViewById(R.id.input_pass_check)
    }
    private val signBtn: Button by lazy {
        findViewById(R.id.btn_sign)
    }
    private val emailRegex: Regex by lazy {
        Regex("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    }
    private val passText: TextView by lazy {
        findViewById(R.id.pass_text)
    }
    private val emails: Array<String> by lazy {
        arrayOf(
            getString(R.string.naver),
            getString(R.string.gmail),
            getString(R.string.kakao),
            getString(R.string.selfaddress)
        )
    }
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var password: String
    private lateinit var email: StringBuilder
    private lateinit var user: User
    private val editTexts
        get() = listOf(
            inputName,
            inputId,
            inputEmail,
            inputPassword,
            inputEmailId,
            inputPasswordCheck
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signBtn.isEnabled = false
        initView()
    }

    private fun initView() {
        userList = UserList.userList

        setTextChangedListener()

        setServiceProvider()

        //focusOut
        setOnFocusChangedListener()
    }

    private fun setTextChangedListener() {
        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setConfirmButtonEnable()
            }
        }
    }

    private fun setServiceProvider() {
        /*이메일 주소 스피너*/
        emailSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emails)
        emailSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (emails[position] != getString(R.string.selfaddress)) {
                    inputEmail.visibility = View.INVISIBLE
                    inputEmail.setText(emails[position])
                } else {
                    inputEmail.visibility = View.VISIBLE
                    inputEmail.setText(null)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun setOnFocusChangedListener() {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                    setConfirmButtonEnable()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() {
        when (this) {
            inputName -> error = getMessageErrorName()
            inputId -> error = getMessageErrorId()
            inputEmail -> error = getMessageErrorEmail()
            inputEmailId -> error = getMessageErrorEmailId()
            inputPassword -> {
                error = getMessageErrorPassword()
                if (inputPassword.text.isEmpty()) passText.visibility = View.VISIBLE
                else passText.visibility = View.INVISIBLE
            }

            inputPasswordCheck -> error = getMessageErrorPasswordCheck()

            else -> Unit
        }
    }

    private fun getMessageErrorName(): String? {
        return if (inputName.text.isEmpty()) getString(R.string.sign_id_error)
        else if (inputName.text.toString().matches(Regex(".*[!@#$%^&*()_+].*"))
        ) getString(R.string.name_error_special)
        else null
    }

    private fun getMessageErrorId(): String? {
        return if (inputId.text.isEmpty()) getString(R.string.sign_id_error)
        else if (inputId.text.toString().length !in 2..8)
            getString(R.string.id_error_length)
        else if (inputId.text.toString().matches(Regex(".*[!@#$%^&*()_+].*"))
        ) getString(R.string.id_error_special)
        else if (userList.any { it.id == inputId.text.toString() })
            getString(R.string.id_error_use)
        else null
    }

    private fun getMessageErrorEmail(): String? {
        return if (inputEmail.text.isEmpty()) getString(R.string.sign_email_address_error)
        else if (inputEmail.text.toString().matches(emailRegex)) null
        else getString(R.string.sign_email_address_error_special)
    }

    private fun getMessageErrorEmailId(): String? {
        return if (inputEmailId.text.isEmpty()) getString(R.string.sign_id_error)
        else if (inputEmailId.text.toString().matches(Regex(".*[!@#$%^&*()_+].*"))
        ) getString(R.string.id_error_special)
        else null
    }

    private fun getMessageErrorPassword(): String? {
        val specialCharacterRegex = Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+")
        val text = inputPassword.text.toString()
        return when {
            text.length !in 10..16 -> getString(R.string.password_error_length)
            specialCharacterRegex.containsMatchIn(text)
                .not() -> getString(R.string.error_special_one)

            inputPassword.text.toString().find { it in 'A'..'Z' } == null
            -> getString(R.string.error_upper_one)

            else -> null
        }
    }

    private fun getMessageErrorPasswordCheck(): String? {
        return if (inputPasswordCheck.text.toString() != inputPassword.text.toString())
            getString(R.string.signe_password_error_nocoincide)
        else null
    }

    private fun setConfirmButtonEnable() {
        signBtn.isEnabled = getMessageErrorName() == null
                && getMessageErrorId() == null
                && getMessageErrorEmail() == null
                && getMessageErrorEmailId() == null
                && getMessageErrorPassword() == null
                && getMessageErrorPasswordCheck() == null
        setSignButtonSendData()
    }

    private fun setSignButtonSendData() {
        signBtn.setOnClickListener {

            name = inputName.getText().toString()
            id = inputId.getText().toString()
            password = inputPassword.getText().toString()
            email = StringBuilder()

            email.append(inputEmailId.text)
            email.append("@")
            email.append(inputEmail.text)
            user = User(name, id, password, email.toString(), null, null, "", null)
            UserList.userList.add(user)
            val intent = Intent(this, SignInActivity::class.java)
            val sendData = intent.apply {
                putExtra("id", id)
                putExtra("password", password)
            }
            setResult(RESULT_OK, sendData)
            finish()
        }
    }
}
