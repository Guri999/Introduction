package com.example.introduction.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        if (type == "Name" && text.includeSpecialCharacters()) return SignUpErrorMessage.NAMESPECIAL

        if (type == "Id" || type == "EmailId" && text.includeSpecialCharacters())
            return SignUpErrorMessage.IDSPECIAL

        return when (type) {
            "Id" -> when {
                text.length !in 2..8 -> SignUpErrorMessage.IDLEGTH
                UserList.userList.any { it.id == text } -> SignUpErrorMessage.IDUSE
                else -> null
            }
            "Email" -> if (!text.validEmailServiceProvider()) SignUpErrorMessage.EMAILSPECIAL else null
            "Password" -> when {
                text.length !in 10..16 -> SignUpErrorMessage.PASSWORDLEGTH
                !text.includeSpecialCharacters() -> SignUpErrorMessage.PASSWORDSPECIAL
                !text.includeUpperCase() -> SignUpErrorMessage.UPPERONE
                else -> null
            }
            else -> null
        }
    }

    fun getEntryData(entryType: SignUpEntryType, userEntity: SignUpUserEntity?){
        _entryType.value = entryType
        _userEntity.value = userEntity
    }

    fun setServiceIndex(service: String?, emails: Array<String>): Int {
        return if (emails.any{ it == service}) {
            emails.indexOf(emails.find { it == service })
        }else emails.lastIndex
    }
}