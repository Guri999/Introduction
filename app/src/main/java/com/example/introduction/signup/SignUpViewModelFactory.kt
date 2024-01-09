package com.example.introduction.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.introduction.UserRepository
import java.lang.IllegalArgumentException

class SignUpViewModelFactory(private val useCase: UseCaseErrorMessage,
                             private val emailIndex: UseCaseServiceIndex,
                             private val passwordCheck: CheckPassword,
                             private val saveUser: SaveUser) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(useCase,emailIndex,passwordCheck,saveUser) as T

        throw IllegalArgumentException("unKnown ViewModel class")
    }
}