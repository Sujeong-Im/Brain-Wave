package com.example.matchingproto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.matchingproto.databinding.ActivityAutoMatchingBinding
import com.example.matchingproto.databinding.ActivityFoundMatchBinding

class FoundMatchActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityFoundMatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_found_match)

        // Activity Binding 객체에 할당 및 View 설정
        mBinding = ActivityFoundMatchBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        mBinding.yesBtn.setOnClickListener {
            // yes 응답 서버로 제출 -> 상대방도 yes를 누를 때까지 대기
        }
        mBinding.noBtn.setOnClickListener {
            val intent = Intent(this@FoundMatchActivity, AutoMatchingActivity::class.java)
            startActivity(intent)
            Toast.makeText(getApplicationContext(), "상대의 거절로 매칭에 실패하였습니다.", Toast.LENGTH_LONG).show();

            // 근데 상대가 no를 눌렀을 때 자동으로 매칭 페이지로 돌아가야 함
        }
    }
}