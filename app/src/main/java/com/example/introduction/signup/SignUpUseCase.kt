package com.example.introduction.signup

import com.example.introduction.UserList
import com.example.introduction.signup.SignUpValidExtension.includeSpecialCharacters
import com.example.introduction.signup.SignUpValidExtension.includeUpperCase
import com.example.introduction.signup.SignUpValidExtension.validEmailServiceProvider

class SignUpUseCase {
    fun setError(type: EditType, text: String): SignUpErrorMessage? {
        val error: SignUpErrorMessage? = when {
            text.isEmpty() -> when (type) {
                EditType.NAME -> SignUpErrorMessage.NAME
                EditType.ID, EditType.EMAIL_ID -> SignUpErrorMessage.ID
                EditType.EMAIL -> SignUpErrorMessage.EMAIL
                EditType.PASSWORD -> SignUpErrorMessage.PASSWORDLEGTH
                else -> null
            }
            type == EditType.NAME && text.includeSpecialCharacters() -> SignUpErrorMessage.NAMESPECIAL
            type in listOf(EditType.ID, EditType.EMAIL_ID) && text.includeSpecialCharacters() -> SignUpErrorMessage.IDSPECIAL
            else -> when (type) {
                EditType.ID -> when {
                    text.length !in 2..8 -> SignUpErrorMessage.IDLEGTH
                    UserList.userList.any { it.id == text } -> SignUpErrorMessage.IDUSE
                    else -> null
                }
                EditType.EMAIL -> if (!text.validEmailServiceProvider()) SignUpErrorMessage.EMAILSPECIAL else null
                EditType.PASSWORD -> when {
                    text.length !in 10..16 -> SignUpErrorMessage.PASSWORDLEGTH
                    !text.includeSpecialCharacters() -> SignUpErrorMessage.PASSWORDSPECIAL
                    !text.includeUpperCase() -> SignUpErrorMessage.UPPERONE
                    else -> null
                }
                else -> null
            }
        }
        return error
    }

    fun setServiceIndex(service: String?, emails: Array<String>): Int {
        return if (emails.any{ it == service}) {
            emails.indexOf(emails.find { it == service })
        }else emails.lastIndex
    }

    fun checkPassword(password: String, confirmPassword: String): SignUpErrorMessage? {
        return if (password == confirmPassword) null
        else SignUpErrorMessage.PASSWORDNOCOINCIDE
    }
}