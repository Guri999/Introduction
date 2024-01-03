package com.example.introduction.user

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.introduction.R
import com.example.introduction.signup.SignUpActivity
import com.example.introduction.signup.SignUpEntryType
import com.example.introduction.signup.SignUpUserEntity

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        findViewById<Button>(R.id.bt_update).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                startActivity(
                    SignUpActivity.newIntent(
                        this@UserActivity,
                        SignUpEntryType.UPDATE,
                        SignUpUserEntity(
                            "스파르타",
                            "hello",
                            "gmail.com"
                        )
                    )
                )
            }
        }
    }
}