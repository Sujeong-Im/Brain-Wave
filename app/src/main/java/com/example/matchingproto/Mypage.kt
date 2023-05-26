package com.example.matchingproto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class Mypage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        var test = findViewById<TextView>(R.id.tv_test)
        var noticeBtn = findViewById<Button>(R.id.btn_notice)

        //임시 저장한 사용자 이메일 불러오기
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val emailAddr = sharedPreferences.getString("email", "")

        test.text = emailAddr

        noticeBtn.setOnClickListener {
            intent = Intent(this, Notice :: class.java)
            startActivity(intent)
            finish()
        }
    }
}