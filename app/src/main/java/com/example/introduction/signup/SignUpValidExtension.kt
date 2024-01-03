package com.example.introduction.signup

object SignUpValidExtension {

    /*
    * 이메일 정규 표현식
    */
    fun String.validEmailServiceProvider() = Regex("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$").containsMatchIn(this)
    /*
    * 특문 포함
    */
    fun String.includeSpecialCharacters() =
        Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+").containsMatchIn(this)

    /*
    * 대문자 포함
    */
    fun String.includeUpperCase() = Regex("[A-Z]").containsMatchIn(this)
}