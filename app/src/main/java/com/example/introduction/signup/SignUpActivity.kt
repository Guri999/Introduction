package com.example.introduction.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.introduction.R
import com.example.introduction.databinding.ActivitySignUpBinding
import com.example.introduction.signup.SignUpEntryType.Companion.getEntryType

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class SignUpActivity : AppCompatActivity() {
    companion object {

        const val EXTRA_ENTRY_TYPE = "extra_entry_type"
        const val EXTRA_USER_ENTITY = "extra_user_entity"

        fun newIntent(
            context: Context,
            entryType: SignUpEntryType,
            entity: SignUpUserEntity
        ): Intent = Intent(context, SignUpActivity()::class.java).apply {
            putExtra(EXTRA_ENTRY_TYPE, entryType.ordinal)
            putExtra(EXTRA_USER_ENTITY, entity)
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

    private val viewModel by lazy {
        ViewModelProvider(this@SignUpActivity)[SignUpViewModel::class.java]
    }
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private var canSpin = false
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

        getEntry()

    }

    /**
     * 엔트리타입을 받고 뷰모델에 갱신
     */
    private fun getEntry() {
        viewModel.getEntryData(
            getEntryType(intent.getIntExtra(EXTRA_ENTRY_TYPE, 0)),
            intent.getParcelableExtra(EXTRA_USER_ENTITY)
            )
        viewModel.entryType.observe(this, Observer { entry ->
            setSignUpBtn(entry)
        })
    }

    /**
     * 버튼의 텍스트를 수정하고 UPDATE상태면 전달받은 데이터를 세팅한다
     */
    private fun setSignUpBtn(entry: SignUpEntryType) {
        when (entry) {
            SignUpEntryType.UPDATE -> {
                binding.apply {
                    etSignupId.visibility = View.GONE
                    tvSignupId.visibility = View.GONE
                    btSignupBtn.setText(R.string.user_update)
                }
                setUserEntity()
            }

            else -> binding.btSignupBtn.setText(R.string.sign_up)
        }
    }

    /**
     * 전달 받은 데이터값을 토대로 EditText에 세팅한다
     */
    private fun setUserEntity() = with(viewModel) {
        userEntity.observe(this@SignUpActivity) {
            binding.apply {
                etSignupName.setText(userEntity.value?.name ?: "")
                etSignupEid.setText(userEntity.value?.email ?: "")
                spSignupSpin.setSelection(setServiceIndex(etSignupEservice.text.toString(),emails))
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

    /**
     * 뷰 모델에서 위젯의 텍스트를 문자열로 구분해서 넣어주고
     * 에러메세지에 표시할 문자를 받는다
     */
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

        binding.tvSignupPassempty.visibility =
            if (binding.etSignupPass.text.isEmpty()) View.VISIBLE else View.GONE
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
                    if (canSpin) {
                        if (viewModel.selectEmail(position, emails)) {
                            etSignupEservice.setText(emails[position])
                            etSignupEservice.visibility = View.GONE
                        } else {
                            etSignupEservice.setText("")
                            etSignupEservice.visibility = View.VISIBLE
                        }
                    }
                    canSpin = true
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

