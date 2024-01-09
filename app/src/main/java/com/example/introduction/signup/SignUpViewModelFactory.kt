package com.example.introduction.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.introduction.UserRepository
import java.lang.IllegalArgumentException

class SignUpViewModelFactory(private val userRepository: UserRepository, private val useCase: SignUpUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(userRepository, useCase) as T

        throw IllegalArgumentException("unKnown ViewModel class")
    }
}