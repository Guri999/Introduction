package com.example.introduction

class UserRepository {
    var userList = ArrayList<User>()

    fun loadUser(userData: ArrayList<User>) {
        userList = userData
    }

    fun findUserById(id: String): User? {
        return userList.find { it.id == id }
    }
    fun updateUser(user: User) {
        userList.find { it.id == user.id }?.let {
            it.name = user.name
            it.password = user.password
            it.email = user.email
        }
    }

    fun saveUser(newUser: User) {
        userList.add(newUser)
    }
}
