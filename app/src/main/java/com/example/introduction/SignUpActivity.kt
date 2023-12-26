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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import kotlin.text.StringBuilder
import com.example.introduction.SignUpValidExtension.validEmailServiceProvider
import com.example.introduction.SignUpValidExtension.includeSpecialCharacters
import com.example.introduction.SignUpValidExtension.includeUpperCase

class SignUpActivity : AppCompatActivity() {

    private val inputName: EditText by lazy {
        findViewById(R.id.input_name)
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

    private val idText: TextView by lazy {
        findViewById(R.id.id_text)
    }
    private fun setEditCheck(){
        if (intent.getStringExtra("editId") != null){
            inputId.isVisible = false
            idText.isVisible = false
            id = intent.getStringExtra("editId")!!
            signBtn.setText(R.string.profile_edit)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signBtn.isEnabled = false
        initView()
    }

    private fun initView() {
        setEditCheck()

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
                    inputEmail.text = null
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
        val text = inputName.text.toString()
        val errorCode = when {
            text.isEmpty() -> SignUpErrorMessage.NAME
            text.includeSpecialCharacters() -> SignUpErrorMessage.NAMESPECIAL
            else -> null
        }
        return errorCode?.let { getString(it.message) }
    }


    private fun getMessageErrorId(): String? {
        val text = inputId.text.toString()
        if (inputId.isVisible) {
            val errorCode = when {
                text.isEmpty() -> SignUpErrorMessage.ID
                text.length !in 2..8 -> SignUpErrorMessage.IDLEGTH
                text.includeSpecialCharacters() -> SignUpErrorMessage.IDSPECIAL
                UserList.userList.any { it.id == inputId.text.toString() } -> SignUpErrorMessage.IDUSE
                else -> null
            }
            return errorCode?.let { getString(it.message) }
        }else return null
    }

    private fun getMessageErrorEmail(): String? {
        val text = inputEmail.text.toString()
        val errorCode = when {
            text.isEmpty() -> SignUpErrorMessage.EMAIL
            !text.validEmailServiceProvider() -> SignUpErrorMessage.EMAILSPECIAL
            else -> null
        }
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorEmailId(): String? {
        val text = inputEmailId.text.toString()
        val errorCode = when {
            text.isEmpty() -> SignUpErrorMessage.ID
            text.includeSpecialCharacters() -> SignUpErrorMessage.IDSPECIAL
            else -> null
        }
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorPassword(): String? {
        val text = inputPassword.text.toString()
        val errorCode = when {
            text.length !in 10..16 -> SignUpErrorMessage.PASSWORDLEGTH
            text.includeSpecialCharacters()
                .not() -> SignUpErrorMessage.PASSWORDSPECIAL

            !text.includeUpperCase()
            -> SignUpErrorMessage.UPPERONE

            else -> null
        }
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorPasswordCheck(): String? {
        return if (inputPasswordCheck.text.toString() != inputPassword.text.toString())
            getString(SignUpErrorMessage.PASSWORDNOCOINCIDE.message)
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
            name = inputName.text.toString()
            if (inputId.isVisible) id = inputId.text.toString()
            password = inputPassword.text.toString()
            email = StringBuilder()

            email.append(inputEmailId.text)
            email.append("@")
            email.append(inputEmail.text)
            if (!inputId.isVisible) {
                UserList.userList.find { it.id == id }?.let {
                    it.name = name
                    it.password = password
                    it.email = email.toString()
                }
                setResult(RESULT_OK,null)
                finish()
            } else {
                val newUser = User(name, id, password, email.toString())
                UserList.userList.add(newUser)
                val intent = Intent(this, SignInActivity::class.java)
                val sendData = intent.apply {
                    putExtra("id", id)
                    putExtra("password", password)
                }
                setResult(RESULT_OK, sendData)
            }
            if(!isFinishing) finish()
        }
    }

}
