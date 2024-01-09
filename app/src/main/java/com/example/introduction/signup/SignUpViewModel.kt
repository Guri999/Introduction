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
    //실드클래스에서 처리함
    private val _errors: MutableMap<EditType, MutableLiveData<SignUpErrorMessage>> = mutableMapOf(
        EditType.NAME to MutableLiveData(),
        EditType.ID to MutableLiveData(),
        EditType.EMAIL_ID to MutableLiveData(),
        EditType.EMAIL to MutableLiveData(),
        EditType.PASSWORD to MutableLiveData(),
        EditType.PASSWORD_CHECK to MutableLiveData()
    )
    val errors: Map<EditType, LiveData<SignUpErrorMessage>> get() = _errors

    /**
     * todo 사용자를 Repository에 저장
     *
     * usecase로 데이터를 보내고 usecase에선 값들을 User타입으로 변환
     * 그후 Repository로 넘겨주고 값을 저장한다
    */
    fun sendData(name: String, id: String, emailId: String, emailService: String, password: String, idVisible: Boolean) {
        useCase.saveUser(name, id, emailId, emailService, password, idVisible)
    }

    /**
     * TODO 에러메세지를 받기
     *
     * Enum Class로 EditText마다 Type을 정해주고 타입에 맞춰서 errors에 넣어주고
     * useCase에서 로직을 실행해 에러 메세지에 넣을 값을 받은 뒤 _errors[type]에 넣어준다
     * 메인 액티비티에선 각 타입에 맞춰 EditText에 에러메시지 출력
     */
    fun getErrorMessage(type: EditType, text: String) {
        _errors[type]?.value = useCase.setError(type, text)
    }

    /**
     * TODO 사용자가 페이지에 입장할때 ENTRY_TYPE을 받고 입장한 유저 데이터를 감지
     */
    fun getEntryData(entryType: SignUpEntryType, userEntity: SignUpUserEntity?){
        _entryType.value = entryType
        _userEntity.value = userEntity
    }

    /**
     * TODO 처음 입장할때 이메일세팅
     *
     * 이메일이 스피너의 목록에 있는지 확인하는 로직을 useCase에서 실행하고
     * 위치값을 갱신한다
     */
    fun setServiceIndex(service: String?, emails: Array<String>) {
        _emailPosition.value = useCase.setServiceIndex(service, emails)
    }

    /**
     * TODO 비밀번호 중복검사
     *
     * 비밀번호가 중복하는지 검사 useCase에서 체크한다
     */
    fun checkPassword(password: String, confirm: String) {
        _errors[EditType.PASSWORD_CHECK]?.value = useCase.checkPassword(password, confirm)
    }

}

/**
 * TODO EditText의 타입
 *
 * EditText에서 텍스트를 뷰모델에 보낼땐 뷰모델은 어느 EditText에서 보냈는지 구분할 방법이 필요함
 * 그래서 EditText에서 담당하는 부분을 나눠서 EditType으로 정의
 */
enum class EditType {
    NAME, ID, EMAIL_ID, EMAIL, PASSWORD, PASSWORD_CHECK, NONE
}