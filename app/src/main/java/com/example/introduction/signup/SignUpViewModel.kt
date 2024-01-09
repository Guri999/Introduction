package com.example.introduction.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.introduction.UserRepository


class SignUpViewModel(private val userRepository: UserRepository, private val useCase: SignUpUseCase): ViewModel() {

    private val _entryType: MutableLiveData<SignUpEntryType> = MutableLiveData()
    val entryType: LiveData<SignUpEntryType> get() = _entryType

    private val _userEntity: MutableLiveData<SignUpUserEntity> = MutableLiveData()
    val userEntity: LiveData<SignUpUserEntity> get() = _userEntity

    private val _nameError: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    val nameError: LiveData<SignUpErrorMessage> get() = _nameError

    private val _idError: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    val idError: LiveData<SignUpErrorMessage> get() = _idError

    private val _emailIdError: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    val emailIdError: LiveData<SignUpErrorMessage> get() = _emailIdError
    private val _emailError: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    val emailError: LiveData<SignUpErrorMessage> get() = _emailError

    private val _passwordError: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    val passwordError: LiveData<SignUpErrorMessage> get() = _passwordError

    private val _passwordChkError: MutableLiveData<SignUpErrorMessage> = MutableLiveData()
    val passwordChkError: LiveData<SignUpErrorMessage> get() = _passwordChkError

    private val _emailPosition: MutableLiveData<Int> = MutableLiveData()
    val emailPosition: LiveData<Int> get() = _emailPosition



    /**
     * UserList조작 하는 부분은 Repository를 추가하는게 좋을듯
     * */
    fun sendData(name: String, id: String, emailId: String, emailService: String, password: String, idVisible: Boolean) {
        userRepository.sendData(name, id, emailId, emailService, password, idVisible)
    }

    fun getErrorMessage(type: EditType, text: String) {
        when (type) {
            EditType.NAME -> _nameError.value = useCase.setError(type, text)
            EditType.ID -> _idError.value = useCase.setError(type,text)
            EditType.EMAIL_ID -> _emailIdError.value = useCase.setError(type, text)
            EditType.EMAIL -> _emailError.value = useCase.setError(type,text)
            EditType.PASSWORD -> _passwordError.value = useCase.setError(type,text)
            else -> null
        }
    }

    fun getEntryData(entryType: SignUpEntryType, userEntity: SignUpUserEntity?){
        _entryType.value = entryType
        _userEntity.value = userEntity
    }

    fun setServiceIndex(service: String?, emails: Array<String>) {
        _emailPosition.value = useCase.setServiceIndex(service, emails)
    }

    fun checkPassword(password: String, confirm: String) {
        _passwordChkError.value = useCase.checkPassword(password, confirm)
    }

}

enum class EditType {
    NAME, ID, EMAIL_ID, EMAIL, PASSWORD, PASSWORD_CHECK, NONE
}