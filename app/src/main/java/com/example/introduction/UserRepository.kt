package com.example.introduction

class UserRepository {
    var userList = ArrayList<User>()

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
}