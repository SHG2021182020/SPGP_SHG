package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin

class PlayerBullet(
    private val scene: MainScene,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float,
    angle: Float,
    private val damage: Int = 10 // 🚨 추가됨: AutoGun이 던져주는 가변 데미지를 여기서 받습니다!
) : IGameObject {

    private val speed = 1200f
    private val radius = 20f
    private val dirX = cos(angle)
    private val dirY = sin(angle)

    // OOM(메모리 누수) 방지용 투사체 수명
    private var lifespan = 2.0f

    private val paint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }

    override fun update(gctx: GameContext) {
        val dt = 1f / 60f

        lifespan -= dt
        if (lifespan <= 0f) {
            scene.world.remove(this, MainScene.Layer.WEAPON)
            return
        }

        worldX += dirX * speed * dt
        worldY += dirY * speed * dt

        val hitRadiusSq = (radius + 50f) * (radius + 50f)

        // 동시성 에러 방지 및 GC 최적화를 위한 플래그 검사
        for (enemy in scene.enemies) {
            if (enemy.isDead) continue

            val dx = enemy.worldX - worldX
            val dy = enemy.worldY - worldY
            if (dx * dx + dy * dy < hitRadiusSq) {
                enemy.takeDamage(damage) // 주입받은 데미지 적용
                scene.world.remove(this, MainScene.Layer.WEAPON)
                return
            }
        }
    }

    override fun draw(canvas: Canvas) {
        val screenX = worldX + gridBg.offsetX
        val screenY = worldY + gridBg.offsetY
        canvas.drawCircle(screenX, screenY, radius, paint)
    }
}