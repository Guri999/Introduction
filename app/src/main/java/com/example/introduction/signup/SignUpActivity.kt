package com.example.introduction.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.introduction.R
import com.example.introduction.databinding.ActivitySignUpBinding
import kotlin.text.StringBuilder

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class SignUpActivity : AppCompatActivity() {

    /**
     * 사용자의 진입상태를 나타내는코드
     *
     *
     */
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

    private val emails: Array<String> by lazy {
        arrayOf(
            getString(R.string.naver),
            getString(R.string.gmail),
            getString(R.string.kakao),
            getString(R.string.selfaddress)
        )
    }
    private val editTexts
        get() = listOf(
            binding.etSignupName,
            binding.etSignupId,
            binding.etSignupEservice,
            binding.etSignupPass,
            binding.etSignupEid,
            binding.etSignupPasschk
        )

    private val userEntity: SignUpUserEntity? by lazy {
        intent?.getParcelableExtra(
            EXTRA_USER_ENTITY, SignUpUserEntity::class.java
        )
    }

    /**
     * ordinal로 변환시켰기땜에
     *
     * 다시 타입을 변경시켜줄 코드가 필요함
     */
    private val entryType: SignUpEntryType by lazy {
        SignUpEntryType.getEntryType(
            intent?.getIntExtra(
                EXTRA_ENTRY_TYPE,
                SignUpEntryType.CREATE.ordinal
            )
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this@SignUpActivity).get(SignUpViewModel::class.java)
    }
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    /**
     * 변수 선언 끝!
     * onCreate
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btSignupBtn.isEnabled = false
        initView()
    }


    private fun initView() {
        setTextChangedListener()

        setServiceProvider()

        setOnFocusChangedListener()

        setUserEntity()

        setSignUpBtn()
    }

    /**
     * 회원가입 버튼 or 수정하기 버튼
     * 유저의 엔트리타입에 따라 다르게 나타나게함
     */
    private fun setSignUpBtn() {
        binding.btSignupBtn.let {
            it.setText(
                when (entryType) {
                    SignUpEntryType.UPDATE -> {
                        binding.etSignupId.visibility = View.GONE
                        binding.tvSignupId.visibility = View.GONE
                        R.string.user_update
                    }
                    else -> R.string.sign_up
                }
            )
        }
    }

    /**
     * 전달 받은 데이터값을 토대로 EditText에 세팅한다
     */
    private fun setUserEntity() = with(viewModel) {
        setUserEntity(this@SignUpActivity.entryType, this@SignUpActivity.userEntity)
        binding.apply {
            userEntity.observe(this@SignUpActivity) {
                etSignupName.setText(userEntity.value?.name ?: "")
                etSignupEid.setText(userEntity.value?.email ?: "")
                etSignupEservice.setText(userEntity.value?.emailService ?: "")
            }
        }
    }

    private fun setOnFocusChangedListener() {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    setErrorMessage(editText)
                    setConfirmButtonEnable()
                }
            }
        }
    }

    private fun setTextChangedListener() {
        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                setErrorMessage(editText)
                setConfirmButtonEnable()
            }
        }
    }

    private fun setErrorMessage(editText: EditText) {
        val errorType = when (editText) {
            binding.etSignupName -> "Name"
            binding.etSignupId -> "Id"
            binding.etSignupEid -> "EmailId"
            binding.etSignupEservice -> "Email"
            binding.etSignupPass -> "Password"
            else -> ""
        }
        val errorCode = viewModel.getErrorMessage(errorType, editText.text.toString())?.message
        editText.error = errorCode?.let { getString(it) }

        binding.tvSignupPassempty.visibility = if (binding.etSignupPass.text.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setServiceProvider() {
        binding.apply {
            spSignupSpin.adapter =
                ArrayAdapter(
                    this@SignUpActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    emails
                )
            spSignupSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (viewModel.selectEmail(position, emails)) {
                        etSignupEservice.setText(emails[position])
                        etSignupEservice.visibility = View.GONE
                    } else {
                        etSignupEservice.setText("")
                        etSignupEservice.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    private fun setConfirmButtonEnable() {
        binding.btSignupBtn.isEnabled =
            editTexts.all { it.error == null } && binding.etSignupPasschk.error == null
        setSignButtonSendData()
    }

    //뷰 모델로 데이터 갱신
    private fun setSignButtonSendData() {
        binding.btSignupBtn.setOnClickListener {
            val name = binding.etSignupName.text.toString()
            val id = binding.etSignupId.text.toString()
            val emailId = binding.etSignupEid.text.toString()
            val emailService = binding.etSignupEservice.text.toString()
            val password = binding.etSignupPass.text.toString()
            val isIdVisible = binding.etSignupId.isVisible

            viewModel.sendData(name, id, emailId, emailService, password, isIdVisible)

            val sendData = intent.apply {
                putExtra("id", id)
                putExtra("password", password)
            }
            setResult(RESULT_OK, sendData)
            if (!isFinishing) finish()
        }
    }
}

