package com.example.introduction.signup

import androidx.annotation.StringRes
import com.example.introduction.R

enum class SignUpErrorMessage(
    @StringRes val message: Int,
){
    NAME(R.string.sign_name_error),
    NAMESPECIAL(R.string.name_error_special),
    ID(R.string.sign_id_error),
    IDLEGTH(R.string.id_error_length),
    IDSPECIAL(R.string.id_error_special),
    IDUSE(R.string.id_error_use),
    EMAIL(R.string.sign_email_address_error),
    EMAILSPECIAL(R.string.sign_email_address_error_special),
    PASSWORDLEGTH(R.string.password_error_length),
    PASSWORDSPECIAL(R.string.error_special_one),
    UPPERONE(R.string.error_upper_one),
    PASSWORDNOCOINCIDE(R.string.signe_password_error_nocoincide),
}