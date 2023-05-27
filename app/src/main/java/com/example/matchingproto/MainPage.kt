package com.example.matchingproto
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainPage : AppCompatActivity() {


    private var backPressedTime: Long = 0
    private val backPressThreshold: Long = 2000 // 두 번 눌러야 하는 시간 간격 (2초)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        var btn = findViewById<ImageButton>(R.id.logBtn)
        btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        var partyBtn = findViewById<Button>(R.id.party_btn)
        var autoBtn = findViewById<Button>(R.id.auto_btn)
        partyBtn.setOnClickListener{
            showAlertDialog(this, "로그인",
                "로그인을 해야 이용할 수 있습니다.")
        }
        autoBtn.setOnClickListener{
            showAlertDialog(this, "로그인",
                "로그인을 해야 이용할 수 있습니다.")
        }

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < backPressThreshold) {
            super.onBackPressed()
            finishAffinity()
            System.exit(0)
        } else {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            backPressedTime = System.currentTimeMillis()
        }
    }

    // 알림 창 표시 함수
    fun showAlertDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인", null)
        val dialog = builder.create()
        dialog.show()
    }

}