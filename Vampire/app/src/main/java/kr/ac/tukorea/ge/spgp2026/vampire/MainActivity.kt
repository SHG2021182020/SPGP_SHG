package kr.ac.tukorea.ge.spgp2026.vampire

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.ac.tukorea.ge.spgp2026.dragonflight.R

// 🚨 주의: 상단에 import java.io.File이나 databinding 관련 코드가 단 한 줄이라도 있으면 안 됩니다.

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}