package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ExpOrb(
    private val scene: MainScene, // Scene 대신 MainScene으로 타입 변경
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float,
    val expAmount: Int = 10
) : IGameObject {

    private val paint = Paint().apply {
        color = Color.CYAN
        isAntiAlias = true
        setShadowLayer(15f, 0f, 0f, Color.BLUE)
    }
    private val radius = 12f
    private var screenX = 0f
    private var screenY = 0f

    private val magnetRadius = 300f
    private val pickupRadius = 50f
    private var speed = 0f
    private val maxSpeed = 800f
    private val acceleration = 1500f

    override fun update(gctx: GameContext) {
        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        val dx = playerWorldX - worldX
        val dy = playerWorldY - worldY

        // 최적화: 제곱근(sqrt) 연산 제거
        val distSq = dx * dx + dy * dy
        val dt = 1f / 60f

        // 자석 범위 판정 (magnetRadius의 제곱과 비교)
        if (distSq < magnetRadius * magnetRadius) {
            speed += acceleration * dt
            if (speed > maxSpeed) speed = maxSpeed

            val angle = atan2(dy.toDouble(), dx.toDouble())
            worldX += (cos(angle) * speed * dt).toFloat()
            worldY += (sin(angle) * speed * dt).toFloat()
        } else {
            speed = 0f
        }

        screenX = worldX + gridBg.offsetX
        screenY = worldY + gridBg.offsetY

        // 획득 범위 판정 (pickupRadius의 제곱과 비교)
        if (distSq < pickupRadius * pickupRadius) {
            scene.player.addExp(expAmount)
            scene.world.remove(this, MainScene.Layer.ITEM)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(screenX, screenY, radius, paint)
    }
}