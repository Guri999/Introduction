package com.example.introduction.signup

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.introduction.R
import com.example.introduction.SignInActivity
import com.example.introduction.User
import com.example.introduction.UserList
import kotlin.text.StringBuilder
import com.example.introduction.signup.SignUpValidExtension.validEmailServiceProvider
import com.example.introduction.signup.SignUpValidExtension.includeSpecialCharacters
import com.example.introduction.signup.SignUpValidExtension.includeUpperCase

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class SignUpActivity : AppCompatActivity() {
    //얘가 어떤 상태로 진입했는지 플래그가 필요함
    companion object {
        const val EXTRA_ENTRY_TYPE = "extra_entry_type"
        const val EXTRA_USER_ENTITY = "extra_user_entity"
        fun newIntent( // 상태를 받기위한 인터페이스 같은것
            context: Context,
            entryType: SignUpEntryType,
            userEntity: SignUpUserEntity
        ): Intent = Intent(
            context,
            SignUpActivity::class.java
        ).apply {
            putExtra(
                EXTRA_ENTRY_TYPE,
                entryType.ordinal
            )//이넘클래스는 풋엑스트라에 못올림, 그래서 타입을 ordinal로 바꿈 (Index값)
            putExtra(EXTRA_USER_ENTITY, userEntity)
        }

    }

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

    private val userEntity: SignUpUserEntity? by lazy {
        intent?.getParcelableExtra(
            EXTRA_USER_ENTITY, SignUpUserEntity::class.java
        )
    }
    private val idText: TextView by lazy {
        findViewById(R.id.id_text)
    }

    private val entryType: SignUpEntryType by lazy {
        SignUpEntryType.getEntryType(
            intent?.getIntExtra(
                EXTRA_ENTRY_TYPE,
                SignUpEntryType.CREATE.ordinal
            ) //IntExtra로 받아서 타입이 다른대 이제 Int형을 enumclass로 컴퍼트하는 코드를 만든다
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this@SignUpActivity).get(SignUpViewModel::class.java)
    }
    /*private fun setEditCheck() {
        if (intent.getStringExtra("editId") != null) {
            inputId.isVisible = false
            idText.isVisible = false
            id = intent.getStringExtra("editId")!!
            signBtn.setText(R.string.profile_edit)
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signBtn.isEnabled = false
        initView()
    }


    private fun initView() {
        /*setEditCheck()*/

        setTextChangedListener()

        setServiceProvider()

        //focusOut
        setOnFocusChangedListener()

        //사용자 정보
        setUserEntity()

        with(signBtn) {
            setText(
                when (entryType) {
                    SignUpEntryType.UPDATE -> R.string.user_update
                    else -> R.string.sign_up
                }
            )
            setOnClickListener {
                if (isConfirmButtonEnable()) {

                }
            }
        }
    }

    /*private fun setUserEntity(){
        if (entryType == SignUpEntryType.CREATE){
            return
        }

        inputName.setText(userEntity?.name)
        inputEmailId.setText(userEntity?.email)

        idText.visibility = View.GONE
        inputId.visibility = View.GONE
        val index = emails.indexOf(userEntity?.emailService) //값이 없으면 -1 리턴
        emailSpinner.setSelection(
            if (index < 0) {
                inputEmail.setText(userEntity?.emailService)
                emails.lastIndex
            }else{
                index
            }
        )
    }*/
    private fun setUserEntity() = with(viewModel) {
        setEmailList(emails)
        userEntityName.observe(this@SignUpActivity) { name ->
            inputName.setText(name)
        }

        userEntityEmailId.observe(this@SignUpActivity) { email ->
            inputEmailId.setText(email)
        }

        idVisibility.observe(this@SignUpActivity) { visibility ->
            idText.visibility = visibility
            inputId.visibility = visibility
        }

        emailSelection.observe(this@SignUpActivity) { selection ->
            emailSpinner.setSelection(selection)
            if (selection == emails.lastIndex) {
                inputEmailId.setText(userEntityEmailId.value ?: "")
            }
        }

        viewModel.setUserEntity(
            SignUpEntryType.getEntryType(intent?.getIntExtra(EXTRA_ENTRY_TYPE, 0)),
            intent?.getParcelableExtra(EXTRA_USER_ENTITY)
        )
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
                    inputEmail.visibility = View.GONE
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

    //뷰모델 에러 메세지 관리가 수월해짐
    private fun getMessageErrorName(): String? {
        val errorCode = viewModel.getMessageErrorName(inputName.text.toString())
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorId(): String? {
        if (inputId.isVisible) {
            val errorCode = viewModel.getMessageErrorId(inputId.text.toString())
            return errorCode?.let { getString(it.message) }
        } else return null
    }

    private fun getMessageErrorEmail(): String? {
        val errorCode = viewModel.getMessageErrorEmail(inputEmail.text.toString())
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorEmailId(): String? {
        val errorCode = viewModel.getMessageErrorEmailId(inputEmailId.text.toString())
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorPassword(): String? {
        val errorCode = viewModel.getMessageErrorPassword(inputPassword.text.toString())
        return errorCode?.let { getString(it.message) }
    }

    private fun getMessageErrorPasswordCheck(): String? {
        return if (inputPasswordCheck.text.toString() != inputPassword.text.toString())
            getString(SignUpErrorMessage.PASSWORDNOCOINCIDE.message)
        else null
    }

    private fun isConfirmButtonEnable() = getMessageErrorName() == null
            && getMessageErrorId() == null
            && getMessageErrorEmail() == null
            && getMessageErrorEmailId() == null
            && getMessageErrorPassword() == null
            && getMessageErrorPasswordCheck() == null

    private fun setConfirmButtonEnable() {
        signBtn.isEnabled = isConfirmButtonEnable()
        setSignButtonSendData()
    }

    //뷰 모델로 데이터 갱신
    private fun setSignButtonSendData() {
        signBtn.setOnClickListener {
            name = inputName.text.toString()
            if (inputId.isVisible) id = inputId.text.toString()
            password = inputPassword.text.toString()
            email = StringBuilder()
            viewModel.setSendData(
                name, id, inputEmailId.text.toString(),
                inputEmail.text.toString(), password, inputId.isVisible
            )
            val sendData = intent.apply {
                putExtra("id", id)
                putExtra("password", password)
            }
            setResult(RESULT_OK, sendData)

            if (!isFinishing) finish()
        }
    }
}
