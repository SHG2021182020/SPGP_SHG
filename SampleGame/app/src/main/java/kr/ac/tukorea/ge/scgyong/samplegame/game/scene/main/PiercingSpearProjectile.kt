package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin

class PiercingSpearProjectile(
    private val scene: MainScene,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float,
    angle: Float,
    private val damage: Int,       // 🚨 수정: 외부 무기가 계산한 데미지를 받음
    private val maxRange: Float    // 🚨 수정: 외부 무기가 계산한 한계 사거리를 받음
) : IGameObject {

    private val speed = 1800f
    private val dirX = cos(angle)
    private val dirY = sin(angle)

    private var traveledDistance = 0f

    // 이미 타격한 적을 기록하는 해시셋 (연타 버그 방지 팩트 로직)
    private val hitEnemies = mutableSetOf<IEnemy>()

    private val paint = Paint().apply {
        color = Color.MAGENTA
        strokeWidth = 15f
        strokeCap = Paint.Cap.ROUND
    }

    override fun update(gctx: GameContext) {
        val dt = 1f / 60f
        val moveAmt = speed * dt
        worldX += dirX * moveAmt
        worldY += dirY * moveAmt
        traveledDistance += moveAmt

        // 사거리를 벗어나면 메모리에서 안전 삭제
        if (traveledDistance > maxRange) {
            scene.world.remove(this, MainScene.Layer.WEAPON)
            return
        }

        // 충돌 검사 (죽은 시체 무시 및 관통 중복 타격 방지)
        for (enemy in scene.enemies) {
            if (enemy.isDead) continue
            if (hitEnemies.contains(enemy)) continue

            val dx = enemy.worldX - worldX
            val dy = enemy.worldY - worldY
            if (dx * dx + dy * dy < 70f * 70f) {
                enemy.takeDamage(damage) // 🚨 주입받은 가변 데미지 적용
                hitEnemies.add(enemy)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        val screenX = worldX + gridBg.offsetX
        val screenY = worldY + gridBg.offsetY

        canvas.drawLine(
            screenX - dirX * 80f, screenY - dirY * 80f,
            screenX + dirX * 80f, screenY + dirY * 80f,
            paint
        )
    }
}