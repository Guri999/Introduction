자기소개 앱 만들기
=

## SignInActivity
![image](https://github.com/Guri999/Introduction/assets/116724657/073b427d-be1c-45d1-85d1-8d146e051faf)

UI는 심플하게 구성

### 로그인
```kotlin
if (UserList.userList.none { it.id == idChk.text.toString() }) Toast.makeText(this,"가입되지 않은 사용자입니다.",Toast.LENGTH_SHORT).show()
            else if (UserList.userList.any { it.id == idChk.text.toString() && it.password == pasChk.text.toString()  }) {
                intent.putExtra("id", idChk.text.toString())
                startActivity(intent)
            }
            else Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다",Toast.LENGTH_SHORT).show()
```
로그인하면 사용자가 가입했는지 안했는지 체크

id랑 password가 등록된값과 같은지 체크후

가입을 안했거나 입력이 잘못됬을경우 토스트 출력


### 회원가입

회원가입 버튼을 누르면 SignUpActivity를 실행

StartActivity말고 StartActivityForResult를 사용

SignUpActivity종료후 가입할때 썻던 아이디와 비밀번호를 가져와

eidtText에 set하도록 갱신하게 했다.

```kotlin
private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val id = result.data?.getStringExtra("id")
            val password = result.data?.getStringExtra("password")

            idChk.setText(id)
            pasChk.setText(password)
        }
    }
...

signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            getContent.launch(intent)
        }
```

## SignUpActivity

![](https://github-production-user-asset-6210df.s3.amazonaws.com/116724657/291019121-6c6a9161-34a5-479d-ab1d-e590bb51a363.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20231217%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20231217T001554Z&X-Amz-Expires=300&X-Amz-Signature=3993d2a8a9389e01a63401709a8b30fe757dbffb76894b0dcc023375f6d66490&X-Amz-SignedHeaders=host&actor_id=116724657&key_id=0&repo_id=732505831)
### TextWatcher
```kotlin
val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

...

inputName.addTextChangedListener(watcher)
        inputId.addTextChangedListener(watcher)
        inputPassword.addTextChangedListener(watcher)
        inputEmail.addTextChangedListener(watcher)
        inputPasswordCheck.addTextChangedListener(watcher)
```
TextWatcher를 사용해서 사용자가 문자를 입력받는걸 실시간으로 체크하고 유효성 검사를 했다

이름 같은 경우 정규 표현식을 활용해서 특수문자가 있는지 체크

> .matches(Regex(".*[!@#$%^&*()_+].*"))
> .matche(일치하는게 있나) []안의 문자중 하나

ID같은경우 글자수와 특수문자 체크하고 UserList에서 이미 사용중인 ID인지 확인한다

### Spinner
```kotlin
val emails = arrayOf("naver.com", "gmail.com", "nate.com", "직접입력")

...
emailSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val email = emails[position]
                if (email != "직접입력") {
                    inputEmail.visibility = View.INVISIBLE
                    inputEmail.setText(email)
                    emailSpinner.visibility = View.VISIBLE
                } else {
                    inputEmail.visibility = View.VISIBLE
                    inputEmail.requestFocus()
                    inputEmail.setText(null)
                    inputEmail.setHint("email.com")
                    emailSpinner.visibility = View.INVISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                inputEmail.visibility = View.INVISIBLE
                inputEmail.setText("naver.com")
            }

        }
```
이메일은 스피너를 이용해서 만들었다

LinearLayout안에 RelativeLayout를 배치해서 메일의 도메인을 적을 EditText와 Spinner를 겹치게 만들고

직접입력을 선택하지 않으면 editText를 숨기고 Spinner로 선택한 값을 숨긴 editText에 set

직접입력을 선택하면 Spinner를 숨기고 editText가 들어나게 했다

> ❓그러면 사용자가 직접입력을 누르면 다시는 스피너를 못쓰는건가?

👉그 문제를 FocusChangeListener를 써서 해결했다

```kotlin
inputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (inputEmail.text.isEmpty()) {
                    inputEmail.error = "올바른 이메일을 입력해주세요."
                    emailSpinner.visibility = View.VISIBLE
                    inputEmail.setHint(null)
                } else {
                    inputEmail.error = null
                    emailSpinner.visibility = View.INVISIBLE
                    inputEmail.setHint("email.com")
                }
            }
        }
```
사용자가 email입력창에서 포커스를 때고 비어있을시 Spinner를 보이게 하고 힌트가 겹치지 힌트를 가렸다.

### 회원가입 버튼

```kotlin
val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
...

signBtn.isEnabled =
                    inputName.error == null && inputId.error == null && inputEmailId.error == null && inputPassword.error == null && inputPasswordCheck.error == null
```
TextWatcher에서 유효성 검사가 끝나고 error가 뜨지 않았을때 활성화하게 만들었다.

```kotlin
signBtn.setOnClickListener {
            val name = inputName.getText().toString()
            val id = inputId.getText().toString()
            val email = StringBuilder()
            email.append(inputEmailId.text)
            email.append("@")
            email.append(inputEmail.text)
            val password = inputPassword.getText().toString()

            val user = User(name, id, password, email.toString(), null, null, "", null)
            val intent = Intent(this, SignInActivity::class.java)
            UserList.userList.add(user)
            val result = intent.apply {
                putExtra("id", id)
                putExtra("password", password)
            }
            setResult(RESULT_OK,result)
            finish()
```
그리고 결과값을 받고 User()형식으로 userList에 add한후

signinActivity가 registerForActivityResult로 콜백했으니 setResult를 사용해 signInActivity에 id와 password를 전달했다.

finish()로 창을 닫으면 signInActivity의 EditText에 방금 가입한 ID와 Password가 입력되게 된다.

## UserList

```kotlin
data class User(
    var name: String,
    val id: String,
    val password: String,
    val email: String,
    var mbti: String?,
    var age: Int?,
    var introduce: String?,
    var birth: String?
)

object UserList {
    var userList = ArrayList<User>()
}
```
싱글톤 패턴을 이용해 user정보를 담아냈다

## HomeActivity
![image](https://github.com/Guri999/Introduction/assets/116724657/39c3c7d1-d69b-46c2-9d8e-1f208726e11d)

사용자의 정보를 담아 냈다

입력한 ID를 바탕으로 사용자의 정보를 나타낸다

조건문으로 null일땐 안뜨게 할 수도 있으나 딱히 쓸말도 없어서 냅뒀다

### RandomImage
```kotlin
fun dataLoad() {

     ...

        val random = ThreadLocalRandom.current().nextInt(1, 6)

        when (random) {
            1 -> img.setImageResource(R.drawable.danger)
            2 -> img.setImageResource(R.drawable.dogsound)
            3 -> img.setImageResource(R.drawable.doolyswelcome)
            4 -> img.setImageResource(R.drawable.pikicast285983125)
            5 -> img.setImageResource(R.drawable.pxfuel)
        }

```
ThreadLocalRandom으로 1~5까지 랜덤하게 숫자를 받은뒤

when문을 사용해 숫자에 맞춰 이미지가 랜덤하게 나오게 했다.

### load
```kotlin
fun dataLoad() {
        val userList: ArrayList<User> = UserList.userList
        val id = intent.getStringExtra("id")
        val name = userList.find { it.id == id }!!.name

        ...

        val userMBTI = findViewById<TextView>(R.id.user_mbti)
        
        ...

        userEmail.setText("${email}")
        userId.setText("ID: ${id}")
        userName.setText("이름: ${name}")
        userAge.setText("나이: ${age}")
        userMBTI.setText("MBTI: ${mbti}")
        introduce.setText("${intro}")
    }
```

signInActivity에서 ID값을 받고 유저리스트에서 찾은뒤 사용자 정보를 받는다

아까 랜덤 한것과 같이 dataload()함수로 묶어 뒀다.

### swipe
```kotlin
val swipe: SwipeRefreshLayout = findViewById(R.id.swipe)

        ...

        swipe.setOnRefreshListener {
            dataLoad()
            swipe.isRefreshing = false
        }
```
스와이프를 넣어서 화면을 튕기면 랜덤하게 이미지가 바뀌게 되고 정보가 갱신된다

## profileActivity

![image](https://github.com/Guri999/Introduction/assets/116724657/cbbfeebc-0fca-4247-93db-be0562eb4985)

### 생년월일
```kotlin
val yearList = (1950..2023).toList().map { it.toString() }
        val monthList = (1..12).toList().map { it.toString() }
        val dateList = (1..31).toList().map { it.toString() }

 ....

val npRange = birth?.split("-")
        npYear.run {
            minValue = 0
            maxValue = yearList.size - 1
            wrapSelectorWheel = false
            displayedValues = yearList.toTypedArray()
            value = yearList.indexOf(npRange?.get(0) ?: "1950")
        }
        npMonth.run {
            minValue = 0
            maxValue = monthList.size - 1
            displayedValues = monthList.toTypedArray()
            value = yearList.indexOf(npRange?.get(1) ?: "1")
        }
        npDay.run {
            minValue = 0
            maxValue = dateList.size - 1
            displayedValues = dateList.toTypedArray()
            value = yearList.indexOf(npRange?.get(2) ?: "1")
        }
```
넘버피커를 이용해서 표현했다.

사용자의 생년월일 정보(birth)가 없을땐 기본값인 1950,0,0이지만

정보가 있으면 생년월일을 기준으로 초기값이 설정된다

birth = "${yearList[npYear.value]}-${monthList[npMonth.value]}-${dateList[npDay.value]}" 이렇게 구성되어있으며

split으로 나눠 넘버피커의 초기값을 구하거나 나이를 구하는대 사용했다.

### 수정

```kotlin
btnEdit.setOnClickListener {

            UserList.userList.find { it.id == id }?.let {
                it.name = inputName.text.toString()
                it.age = LocalDate.now().year - yearList[npYear.value].toInt() + 1
                it.introduce = inputIntro.text.toString()
                it.mbti = mbti
                it.birth = "${yearList[npYear.value]}-${monthList[npMonth.value]}-${dateList[npDay.value]}"
            }
            val intent = Intent(this, HomeActivity::class.java)
            setResult(Activity.RESULT_OK, null)
            finish()
        }
```
수정된 정보는 수정 버튼을 눌렀을때

UserList에서 같은 ID를 가진 리스트를 찾은후 바꾼다.
