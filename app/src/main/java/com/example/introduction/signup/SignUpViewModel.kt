package com.example.introduction.signup

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.introduction.User
import com.example.introduction.UserList
import com.example.introduction.signup.SignUpValidExtension.validEmailServiceProvider
import com.example.introduction.signup.SignUpValidExtension.includeSpecialCharacters
import com.example.introduction.signup.SignUpValidExtension.includeUpperCase


class SignUpViewModel : ViewModel() {
    private val _entryType: MutableLiveData<SignUpEntryType> = MutableLiveData()

    private val _userEntityName: MutableLiveData<String> = MutableLiveData()
    val userEntityName: LiveData<String> get() = _userEntityName

    private val _userEntityEmailId: MutableLiveData<String> = MutableLiveData()
    val userEntityEmailId: LiveData<String> get() = _userEntityEmailId

    private val _idVisibility: MutableLiveData<Int> = MutableLiveData()
    val idVisibility: LiveData<Int> get() = _idVisibility

    private val _emailSelection: MutableLiveData<Int> = MutableLiveData()
    val emailSelection: LiveData<Int> get() = _emailSelection
    private val _emailList: MutableLiveData<Array<String>> = MutableLiveData()
    private val _userEntityEmail: MutableLiveData<String> = MutableLiveData()

    fun setEmailList(emails: Array<String>) {
        _emailList.value = emails
    }
    fun setUserEntity(entryType: SignUpEntryType, userEntity: SignUpUserEntity?) {
        _entryType.value = entryType

        when (entryType) {
            SignUpEntryType.CREATE -> return
            SignUpEntryType.UPDATE -> {

                _userEntityName.value = userEntity?.name
                _userEntityEmailId.value = userEntity?.email

                _idVisibility.value = View.GONE

                val emailList = _emailList.value

                if (emailList != null) {
                    val index = emailList.indexOf(userEntity?.emailService) //값이 없으면 -1 리턴
                    _emailSelection.value = if (index < 0) {
                        _userEntityEmail.value = userEntity?.emailService
                        emailList.lastIndex
                    } else index
                }
            }
            else -> return
        }
    }

    fun getMessageErrorName(name: String): SignUpErrorMessage? {
        return when {
            name.isEmpty() -> SignUpErrorMessage.NAME
            name.includeSpecialCharacters() -> SignUpErrorMessage.NAMESPECIAL
            else -> null
        }
    }

    fun getMessageErrorId(id: String): SignUpErrorMessage? {
        return when {
            id.isEmpty() -> SignUpErrorMessage.ID
            id.length !in 2..8 -> SignUpErrorMessage.IDLEGTH
            id.includeSpecialCharacters() -> SignUpErrorMessage.IDSPECIAL
            UserList.userList.any { it.id == id } -> SignUpErrorMessage.IDUSE
            else -> null
        }
    }

    fun getMessageErrorEmail(email: String): SignUpErrorMessage? {
        return when {
            email.isEmpty() -> SignUpErrorMessage.EMAIL
            email.validEmailServiceProvider().not() -> SignUpErrorMessage.EMAILSPECIAL
            else -> null
        }
    }

    fun getMessageErrorEmailId(emailId: String): SignUpErrorMessage? {
        return when {
            emailId.isEmpty() -> SignUpErrorMessage.ID
            emailId.includeSpecialCharacters() -> SignUpErrorMessage.IDSPECIAL
            else -> null
        }
    }

    fun getMessageErrorPassword(password: String): SignUpErrorMessage? {
        return when {
            password.length !in 10..16 -> SignUpErrorMessage.PASSWORDLEGTH
            password.includeSpecialCharacters()
                .not() -> SignUpErrorMessage.PASSWORDSPECIAL
            password.includeUpperCase().not()
            -> SignUpErrorMessage.UPPERONE
            else -> null
        }
    }

    fun setSendData(name:String,id:String,emailId: String,emailService:String,password: String, idVisible: Boolean) {
        val email = StringBuilder()

        email.append(emailId)
        email.append("@")
        email.append(emailService)
        if (idVisible.not()) {
            UserList.userList.find { it.id == id }?.let {
                it.name = name
                it.password = password
                it.email = email.toString()
            }
        } else {
            val newUser = User(name, id, password, email.toString())
            UserList.userList.add(newUser)
        }
    }
}