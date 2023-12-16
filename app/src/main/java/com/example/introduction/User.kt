package com.example.introduction

import android.os.Parcel
import android.os.Parcelable

data class User(
    var name: String,
    val id: String,
    val password: String,
    val email: String,
    var mbti: String?,
    var age: Int?,
    var introduce: String?,
    var birth: String?
)

object UserList {
    var userList = ArrayList<User>()
}

