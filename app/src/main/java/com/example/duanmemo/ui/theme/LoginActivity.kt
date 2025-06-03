package com.example.duanmemo.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.duanmemo.R
import PrefsUtil

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // 已登录则直接跳转主页
        if (PrefsUtil.isLoggedIn(this)) {
            startMainActivity()
            finish()
            return
        }

        // 绑定控件
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)

        // 设置登录按钮点击事件
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 简单验证：用户名和密码不为空
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 模拟登录成功（实际项目中应验证用户名密码）
            PrefsUtil.setLoggedIn(this, true, username)
            startMainActivity()
            finish()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // 添加淡入淡出动画
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}