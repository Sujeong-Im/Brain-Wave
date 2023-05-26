package com.example.matchingproto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : ComponentActivity() {
    val db = Firebase.firestore
    private val TAG = "Register"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        var emailR = findViewById<EditText>(R.id.et_email_r)
        var pwdR = findViewById<EditText>(R.id.et_passwd_r)
        var nicknameR = findViewById<EditText>(R.id.et_nickname_r)
        var ageR = findViewById<EditText>(R.id.et_age_r)
        var registerBtn = findViewById<Button>(R.id.btn_registerc)
        var genderRg = findViewById<RadioGroup>(R.id.gender_btn)

        //회원가입 버튼
        registerBtn.setOnClickListener {

            var em = emailR.text.toString()
            var pwd = pwdR.text.toString()
            var nn = nicknameR.text.toString()
            var age = ageR.text.toString().toIntOrNull()
            var gender = ""

            //나이 입력 안할 시
            if(age == null){
                showAlertDialog(this, "회원가입 실패",
                    "나이를 입력해 주세요.")
                return@setOnClickListener
            }

            //닉네임 안 적을시
            if (nn == "") {
                showAlertDialog(this, "회원가입 실패",
                    "닉네임을 입력해 주세요.")
                return@setOnClickListener
            }

            // 선택된 라디오 버튼 ID 가져오기
            val selectedRadioButtonId = genderRg.checkedRadioButtonId

            try {
                //성별 선택했을 때
                if (selectedRadioButtonId != -1) {
                    // 선택된 라디오 버튼 가져오기
                    val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

                    // 선택된 라디오 버튼의 텍스트 가져오기 (성별)
                    var gendert = selectedRadioButton.text.toString()
                    gender = gendert
                } else {    //선택 안했을 때
                    throw Exception("성별을 선택해 주세요.")
                }
            } catch(e : Exception){
                val errorMessage = e.message
                showAlertDialog(this, "회원가입 실패",
                    "성별을 선택해 주세요.")
                return@setOnClickListener
            }


            // FirebaseAuth 객체 생성
            val auth = FirebaseAuth.getInstance()

            // Firebase에 이미 존재하는 계정인지 확인
            auth.fetchSignInMethodsForEmail(em)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        if (signInMethods != null && signInMethods.isNotEmpty()) {
                            // 이미 계정이 존재하는 경우
                            showAlertDialog(this, "회원가입 실패",
                                "이미 해당 이메일로 가입된 계정이 있습니다.")
                        }
                    }
                }

            // 이메일과 비밀번호를 사용하여 새로운 사용자 등록
            auth.createUserWithEmailAndPassword(em, pwd)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {

                        // 사용자 등록 성공
                        val user = auth.currentUser

                        val newDocRef = db.collection("User_Info").document(em)
                        val data = hashMapOf(
                            "Age" to age,
                            "Gender" to gender,
                            "ID" to em,
                            "Nickname" to nn,
                            "Recommendation" to 0
                        )

                        newDocRef.set(data)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                        // 추가적인 사용자 정보 설정 또는 다른 작업 수행
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        //다음 activity로 넘어갈 때 현재 액티비티 종료
                        finish()

                    } else {
                        // 사용자 등록 실패
                        val exception = task.exception

                        // 실패 이유에 대한 처리
                        //비밀번호 6자리 미만일시
                        if(pwd.length <6){
                            val errorMessage = task.exception?.message ?: "회원가입에 실패했습니다."
                            showAlertDialog(this, "회원가입 실패",
                                "비밀번호는 6자(영문 기준) 이상이어야 합니다.")
                        }

                        //이메일 형식이 아닐시
                        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
                        if(!em.matches(emailPattern)){
                            val errorMessage = task.exception?.message ?: "회원가입에 실패했습니다."
                            showAlertDialog(this, "회원가입 실패",
                                "올바른 이메일을 입력해야 합니다.")
                        }
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