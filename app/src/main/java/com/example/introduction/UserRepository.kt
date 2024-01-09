package com.example.introduction

/**
 * TODO 파사드 패턴으로 Repository 구성
 *
 * 사용자는 함수의 복잡한 로직을 알 필요 없이 함수만 보고 사용가능하다
 */
interface UserData {

    // 모든 유저 데이터 갱신
    fun loadUser(userData: ArrayList<User>)
    // 유저데이터에서 입력받은 id로 유저를 찾아냄
    fun findUserById(id: String): User?
    // 기존 유저의 정보를 업데이트함
    fun updateUser(user: User)
    // 새로운 유저의 정보를 저장함
    fun saveUser(newUser: User)
}
class UserRepository: UserData {
    var userList = ArrayList<User>()

    override fun loadUser(userData: ArrayList<User>) {
        userList = userData
    }

    override fun findUserById(id: String): User? {
        return userList.find { it.id == id }
    }
    override fun updateUser(user: User) {
        userList.find { it.id == user.id }?.let {
            it.name = user.name
            it.password = user.password
            it.email = user.email
        }
    }

    override fun saveUser(newUser: User) {
        userList.add(newUser)
    }
}
