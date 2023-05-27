package com.example.matchingproto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class Notice : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        var back = findViewById<ImageButton>(R.id.backBtn)

        back.setOnClickListener {
            intent = Intent(this, Mypage ::class.java)
            startActivity(intent)
            finish()
        }
    }
}