package com.example.introduction.signup

import com.example.introduction.User
import com.example.introduction.UserList
import com.example.introduction.UserRepository
import com.example.introduction.signup.SignUpValidExtension.includeSpecialCharacters
import com.example.introduction.signup.SignUpValidExtension.includeUpperCase
import com.example.introduction.signup.SignUpValidExtension.validEmailServiceProvider

/**
 * TODO 회원가입&수정 페이지에서 유효성검사
 *
 * EditText의 TYPE을 받고 그에 맞게 유효성을 검사하고 메세지를 반환함
 */
class UseCaseErrorMessage {
    operator fun invoke(type: EditType, text: String): SignUpErrorMessage? {
        val error: SignUpErrorMessage? = when {
            text.isEmpty() -> when (type) {
                EditType.NAME -> SignUpErrorMessage.NAME
                EditType.ID, EditType.EMAIL_ID -> SignUpErrorMessage.ID
                EditType.EMAIL -> SignUpErrorMessage.EMAIL
                EditType.PASSWORD -> SignUpErrorMessage.PASSWORDLEGTH
                else -> null
            }

            type == EditType.NAME && text.includeSpecialCharacters() -> SignUpErrorMessage.NAMESPECIAL
            type in listOf(
                EditType.ID,
                EditType.EMAIL_ID
            ) && text.includeSpecialCharacters() -> SignUpErrorMessage.IDSPECIAL

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
}

/**
 * TODO 이메일 스피너 위치 반환
 *
 * 사용자가 회원가입이아닌 수정으로 Entry했을때 정보를 바탕으로 사용자의 email service가 이메일 스피너에 있는 service인지 확인하고
 * 만약 스피너에 없으면 직접 입력을 있으면 그위치에 맞는 스피너를 세팅하게 한다
 */
class UseCaseServiceIndex{
    operator fun invoke(service: String?, emails: Array<String>): Int {
        return if (emails.any { it == service }) {
            emails.indexOf(emails.find { it == service })
        } else emails.lastIndex
    }
}

/**
 * TODO 비밀번호 확인
 */
class CheckPassword {
    operator fun invoke(password: String, confirmPassword: String): SignUpErrorMessage? {
        return if (password == confirmPassword) null
        else SignUpErrorMessage.PASSWORDNOCOINCIDE
    }
}

/**
 * TODO 신규 사용자 데이터 레포지토리에 저장
 */
class SaveUser(private val userRepository: UserRepository) {
    operator fun invoke(name: String, id: String, emailId: String, emailService: String, password: String, idVisible: Boolean) {
        val email = "$emailId@$emailService"

        if (!idVisible) {
            val user = userRepository.findUserById(id)
            user?.let {
                it.name = name
                it.password = password
                it.email = email
                userRepository.updateUser(it)
            }
        } else {
            val newUser = User(name, id, password, email)
            userRepository.saveUser(newUser)
        }
    }
}