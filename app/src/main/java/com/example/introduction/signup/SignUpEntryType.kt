package com.example.introduction.signup

enum class SignUpEntryType {
    CREATE,
    UPDATE,
    DELETE,
    READ
    ;

    /**
     * getIntExtra로 정수타입으로 받아온
     *
     * 정보를 다시 EntryType으로 전환하자!
     */
    companion object {
        fun getEntryType(ordinal: Int?): SignUpEntryType =
            SignUpEntryType.values().firstOrNull {
                it.ordinal == ordinal
            } ?: CREATE
    }
}
