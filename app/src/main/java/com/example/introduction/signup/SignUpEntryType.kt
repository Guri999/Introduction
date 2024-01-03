package com.example.introduction.signup

enum class SignUpEntryType {
    CREATE,
    UPDATE,
    DELETE,
    READ
    ;

    companion object{
        fun  getEntryType(
            ordinal: Int? // 널에이블로 선언
        ): SignUpEntryType {
            return SignUpEntryType.values().firstOrNull(){
                it.ordinal == ordinal //값이 없으면 널을 반환하고 CREATE
            } ?: CREATE
        }
    }
}