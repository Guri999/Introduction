package com.example.introduction

import android.os.Parcel
import android.os.Parcelable

data class User(
    var name: String,
    val id: String,
    var password: String,
    var email: String,
    var mbti: String? = null,
    var age: Int? = null,
    var introduce: String? = null,
    var birth: String? = null
)

object UserList {
    var userList = ArrayList<User>()
}

