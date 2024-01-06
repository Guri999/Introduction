package com.example.introduction.signup

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.introduction.R
import com.example.introduction.User
import com.example.introduction.UserList
import com.example.introduction.signup.SignUpValidExtension.validEmailServiceProvider
import com.example.introduction.signup.SignUpValidExtension.includeSpecialCharacters
import com.example.introduction.signup.SignUpValidExtension.includeUpperCase


class SignUpViewModel (application: Application): AndroidViewModel(application) {

    private val _entryType: MutableLiveData<SignUpEntryType> = MutableLiveData()
    val entryType: LiveData<SignUpEntryType> get() = _entryType

    private val _userEntity: MutableLiveData<SignUpUserEntity> = MutableLiveData()
    val userEntity: LiveData<SignUpUserEntity> get() = _userEntity

    fun selectEmail(position: Int,emails: Array<String>): Boolean {
        return position != emails.lastIndex
    }

    fun setUserEntity(entryType: SignUpEntryType, userEntity: SignUpUserEntity?) {
        _entryType.value = entryType
        when (entryType) {
            SignUpEntryType.CREATE -> return
            SignUpEntryType.UPDATE -> _userEntity.value = userEntity
            else -> return
        }
    }

    fun sendData(name: String, id: String, emailId: String, emailService: String, password: String, idVisible: Boolean) {
        val email = "$emailId@$emailService"

        if (!idVisible) {
            UserList.userList.find { it.id == id }?.let {
                it.name = name
                it.password = password
                it.email = email
            }
        } else {
            val newUser = User(name, id, password, email)
            UserList.userList.add(newUser)
        }
    }

    fun getErrorMessage(type: String, text: String): SignUpErrorMessage? {
        if (text.isEmpty()) return when (type) {
            "Name" -> SignUpErrorMessage.NAME
            "Id", "EmailId" -> SignUpErrorMessage.ID
            "Email" -> SignUpErrorMessage.EMAIL
            "Password" -> SignUpErrorMessage.PASSWORDLEGTH
            else -> null
        }

        if (type == "Id" || type == "Name" || type == "EmailId" && text.includeSpecialCharacters())
            return SignUpErrorMessage.IDSPECIAL

        return when (type) {
            "Id" -> {
                if (text.length !in 2..8) SignUpErrorMessage.IDLEGTH
                else if (UserList.userList.any { it.id == text }) SignUpErrorMessage.IDUSE
                else null
            }
            "Email" -> if (!text.validEmailServiceProvider()) SignUpErrorMessage.EMAILSPECIAL else null
            "Password" -> {
                if (text.length !in 10..16) SignUpErrorMessage.PASSWORDLEGTH
                else if (!text.includeSpecialCharacters()) SignUpErrorMessage.PASSWORDSPECIAL
                else if (!text.includeUpperCase()) SignUpErrorMessage.UPPERONE
                else null
            }
            else -> null
        }
    }
}