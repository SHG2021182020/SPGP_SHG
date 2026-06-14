package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent

class ResultUi(
    private val screenW: Float,
    private val screenH: Float,
    private val isPlayerDead: Boolean,
    private val playTime: Float,
    private val level: Int,
    private val killCount: Int,
    private val onRestart: () -> Unit,
    private val onQuit: () -> Unit
) {
    private val bgPaint = Paint().apply { color = Color.parseColor("#CC000000"); style = Paint.Style.FILL }
    private val textPaint = Paint().apply { color = Color.WHITE; textAlign = Paint.Align.CENTER; isAntiAlias = true; isFakeBoldText = true }
    private val buttonPaint = Paint().apply { color = Color.DKGRAY; style = Paint.Style.FILL; isAntiAlias = true }

    // 논리적 기준 좌표
    private val cx = screenW / 2f
    private val cy = screenH / 2f

    private val btnWidth = 300f
    private val btnHeight = 100f
    private val btnY = cy + 300f

    private val restartLeft = cx - 350f
    private val restartRight = restartLeft + btnWidth
    private val quitLeft = cx + 50f
    private val quitRight = quitLeft + btnWidth

    // 🚨 핵심: 물리적 픽셀을 논리적 픽셀로 역산하기 위한 캐싱 변수
    private var scale = 1f
    private var offsetX = 0f
    private var offsetY = 0f

    fun draw(canvas: Canvas) {
        // 1. 현재 화면에 그려지는 캔버스의 실제 물리적 픽셀 크기 추출
        val physW = canvas.width.toFloat()
        val physH = canvas.height.toFloat()

        // 2. 엔진의 Fit-Center 레터박싱 비율 및 여백 수학적 역산
        val scaleX = physW / screenW
        val scaleY = physH / screenH
        scale = minOf(scaleX, scaleY)
        offsetX = (physW - screenW * scale) / 2f
        offsetY = (physH - screenH * scale) / 2f

        canvas.drawRect(0f, 0f, screenW, screenH, bgPaint)

        textPaint.textSize = 120f
        textPaint.color = if (isPlayerDead) Color.RED else Color.YELLOW
        val titleText = if (isPlayerDead) "GAME OVER" else "SURVIVAL SUCCESS"
        canvas.drawText(titleText, cx, cy - 200f, textPaint)

        textPaint.textSize = 60f
        textPaint.color = Color.WHITE
        val minutes = (playTime / 60).toInt()
        val seconds = (playTime % 60).toInt()
        canvas.drawText("생존 시간: %02d:%02d".format(minutes, seconds), cx, cy, textPaint)
        canvas.drawText("달성 레벨: Lv.$level", cx, cy + 100f, textPaint)
        canvas.drawText("처치한 좀비: ${killCount}마리", cx, cy + 200f, textPaint)

        val textY = btnY + 65f

        canvas.drawRect(restartLeft, btnY, restartRight, btnY + btnHeight, buttonPaint)
        textPaint.textSize = 50f
        canvas.drawText("재시작", (restartLeft + restartRight) / 2f, textY, textPaint)

        canvas.drawRect(quitLeft, btnY, quitRight, btnY + btnHeight, buttonPaint)
        canvas.drawText("종료", (quitLeft + quitRight) / 2f, textY, textPaint)
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // 🚨 팩트: 물리적 터치 좌표를 엔진의 논리적 캔버스 가상 좌표로 완벽하게 변환 (오차율 0%)
            val ex = (event.x - offsetX) / scale
            val ey = (event.y - offsetY) / scale

            if (ey >= btnY && ey <= btnY + btnHeight) {
                if (ex >= restartLeft && ex <= restartRight) {
                    onRestart()
                    return true
                } else if (ex >= quitLeft && ex <= quitRight) {
                    onQuit()
                    return true
                }
            }
        }
        return true
    }
}