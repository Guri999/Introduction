package com.example.introduction.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.introduction.UserRepository


class SignUpViewModel(private val useCase: SignUpUseCase): ViewModel() {

    private val _entryType: MutableLiveData<SignUpEntryType> = MutableLiveData()
    val entryType: LiveData<SignUpEntryType> get() = _entryType

    private val _userEntity: MutableLiveData<SignUpUserEntity> = MutableLiveData()
    val userEntity: LiveData<SignUpUserEntity> get() = _userEntity

    private val _emailPosition: MutableLiveData<Int> = MutableLiveData()
    val emailPosition: LiveData<Int> get() = _emailPosition

    private val _errors: MutableMap<EditType, MutableLiveData<SignUpErrorMessage>> = mutableMapOf(
        EditType.NAME to MutableLiveData(),
        EditType.ID to MutableLiveData(),
        EditType.EMAIL_ID to MutableLiveData(),
        EditType.EMAIL to MutableLiveData(),
        EditType.PASSWORD to MutableLiveData(),
        EditType.PASSWORD_CHECK to MutableLiveData()
    )
    val errors: Map<EditType, LiveData<SignUpErrorMessage>> get() = _errors

    fun sendData(name: String, id: String, emailId: String, emailService: String, password: String, idVisible: Boolean) {
        useCase.saveUser(name, id, emailId, emailService, password, idVisible)
    }

    fun getErrorMessage(type: EditType, text: String) {
        _errors[type]?.value = useCase.setError(type, text)
    }

    fun getEntryData(entryType: SignUpEntryType, userEntity: SignUpUserEntity?){
        _entryType.value = entryType
        _userEntity.value = userEntity
    }

    fun setServiceIndex(service: String?, emails: Array<String>) {
        _emailPosition.value = useCase.setServiceIndex(service, emails)
    }

    fun checkPassword(password: String, confirm: String) {
        _errors[EditType.PASSWORD_CHECK]?.value = useCase.checkPassword(password, confirm)
    }

}

enum class EditType {
    NAME, ID, EMAIL_ID, EMAIL, PASSWORD, PASSWORD_CHECK, NONE
}