package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin

class EnemyBullet(
    private val scene: MainScene, // 🚨 허점 해결 1: 플레이어 데이터에 접근하기 위해 MainScene 참조 획득
    private val gctx: GameContext,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float,
    angle: Float
) : IGameObject {

    private val speed = 800f
    private val radius = 15f
    private val dirX = cos(angle)
    private val dirY = sin(angle)

    // 🚨 허점 해결 2: MainScene의 World 레이어에서 메모리를 완전 삭제할 수 있도록 플래그 도입
    var isDead = false
    private var lifespan = 3.0f

    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        if (isDead) return // 이미 소멸 마킹된 총알은 연산 즉시 중단

        val dt = 1f / 60f

        lifespan -= dt
        if (lifespan <= 0f) {
            isDead = true // 임시 가림 처리가 아닌 완벽한 사망(삭제 대기) 마킹
            return
        }

        worldX += dirX * speed * dt
        worldY += dirY * speed * dt

        // 🚨 허점 해결 3: 플레이어와의 수학적 거리 계산 및 피격 함수 호출
        if (!scene.player.isDead) {
            // 배경 오프셋을 역산한 플레이어의 절대 월드 좌표 계산
            val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
            val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

            val dx = worldX - playerWorldX
            val dy = worldY - playerWorldY

            // 플레이어 히트박스 반경 (예: 30픽셀 기준 제곱 비교)
            if (dx * dx + dy * dy <= 30f * 30f) {
                scene.player.takeDamage(15) // 플레이어에게 15 데미지 입힘
                isDead = true // 관통되지 않도록 총알 객체 즉시 소멸
            }
        }
    }

    override fun draw(canvas: Canvas) {
        if (isDead) return

        val screenX = worldX + gridBg.offsetX
        val screenY = worldY + gridBg.offsetY
        canvas.drawCircle(screenX, screenY, radius, paint)
    }
}