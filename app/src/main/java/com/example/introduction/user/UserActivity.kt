package com.example.introduction.user

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.introduction.R
import com.example.introduction.databinding.ActivityUserBinding
import com.example.introduction.signup.SignUpActivity
import com.example.introduction.signup.SignUpEntryType
import com.example.introduction.signup.SignUpUserEntity
import com.example.introduction.signup.SignUpViewModel

class UserActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    private val viewModel by lazy { ViewModelProvider(this)[SignUpViewModel::class.java] }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btUpdate.setOnClickListener {
            startActivity( SignUpActivity.newIntent(
                this, SignUpEntryType.UPDATE, SignUpUserEntity("스파르타","hello","r.com")
            ))
        }
    }
}