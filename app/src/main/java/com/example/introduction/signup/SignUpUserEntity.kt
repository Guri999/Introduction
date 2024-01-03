package com.example.introduction.signup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize // 파셀러블 타입이 되야 EXTRA로 옮김
data class SignUpUserEntity (
    val name: String?,
    val email: String?,
    val emailService: String?
) : Parcelable