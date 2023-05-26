package com.example.matchingproto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        var auth = FirebaseAuth.getInstance()
        var login = findViewById<Button>(R.id.btn_login)
        var register = findViewById<Button>(R.id.btn_register)
        var etemail = findViewById<EditText>(R.id.et_email)
        var etpasswd = findViewById<EditText>(R.id.et_passwd)

        //회원가입 버튼
        register.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }

        //로그인 버튼
        login.setOnClickListener {
            //입력값 가져오기
            var user_email = etemail.text.toString()
            var pwd = etpasswd.text.toString()

            //FirebaseAuth 진행
            auth.signInWithEmailAndPassword(user_email, pwd)
                .addOnCompleteListener { task ->
                    //로그인 성공
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        val intent = Intent(this,Main_login::class.java)

                        //사용자 이메일 임시저장
                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", user_email)
                        editor.apply()

                        startActivity(intent)
                        finish()
                    } else {
                        // 로그인 실패
                        val errorMessage = task.exception?.message ?: "로그인에 실패했습니다."
                        showAlertDialog(this, "로그인 실패",
                            "이메일 혹은 비밀번호가 일치하지 않습니다.")
                    }
                }
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