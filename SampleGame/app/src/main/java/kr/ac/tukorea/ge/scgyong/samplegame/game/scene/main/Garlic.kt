package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Garlic(
    private val scene: MainScene,
    private val gridBg: GridBackground
) : IGameObject {

    var weaponLevel = 1

    private var tickTimer = 0f
    private val tickRate = 1.0f

    private val radius: Float get() = 150f + (weaponLevel * 25f)
    private val damage: Int get() = 1 + (weaponLevel * 3)

    // 🚨 수정 포인트 1: 논리적 화면 중앙 좌표를 저장할 멤버 변수 추가
    private var screenX = 0f
    private var screenY = 0f

    private val paint = Paint().apply {
        color = Color.parseColor("#4488FF88")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        // 🚨 수정 포인트 2: 매 프레임 전달되는 gctx를 통해 완벽한 논리적 중앙값을 갱신
        screenX = gctx.metrics.width / 2f
        screenY = gctx.metrics.height / 2f

        val dt = 1f / 60f
        tickTimer += dt

        if (tickTimer >= tickRate) {
            applyAreaDamage(gctx)
            tickTimer = 0f
        }
    }

    private fun applyAreaDamage(gctx: GameContext) {
        if (scene.enemies.isEmpty()) return

        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f
        val radiusSq = radius * radius

        for (enemy in scene.enemies) {
            if (enemy.isDead) continue
            val dx = enemy.worldX - playerWorldX
            val dy = enemy.worldY - playerWorldY

            if (dx * dx + dy * dy <= radiusSq) {
                enemy.takeDamage(damage)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        // 최초 프레임(update 실행 전) 예외 처리
        if (screenX == 0f && screenY == 0f) return

        // 🚨 수정 포인트 3: 물리적 캔버스 크기가 아닌, update에서 갱신한 논리적 중앙 좌표 사용
        canvas.drawCircle(screenX, screenY, radius, paint)
    }
}