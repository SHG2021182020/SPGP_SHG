package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.atan2

class PiercingSpearWeapon(
    private val scene: MainScene,
    private val gridBg: GridBackground
) : IGameObject {

    var weaponLevel = 1

    private var fireTimer = 0f

    // 레벨에 연동되는 스탯 팩트 공식
    val spearDamage: Int get() = 5 + (weaponLevel * 6)
    val fireRate: Float get() = maxOf(0.5f, 3.0f - (weaponLevel * 0.1f))
    val maxRange: Float get() = 1600f + (weaponLevel * 100f)

    override fun update(gctx: GameContext) {
        // UI 창이 열려있다면 게임 루프 진행을 멈춤
        if (scene.isUiOverlayActive) return

        val dt = 1f / 60f
        fireTimer += dt

        if (fireTimer >= fireRate) {
            fire(gctx)
            fireTimer = 0f
        }
    }

    private fun fire(gctx: GameContext) {
        if (scene.enemies.isEmpty()) return

        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        var closestEnemy: IEnemy? = null
        var minDistanceSq = 1200f * 1200f

        for (enemy in scene.enemies) {
            if (enemy.isDead) continue
            val dx = enemy.worldX - playerWorldX
            val dy = enemy.worldY - playerWorldY
            val distSq = dx * dx + dy * dy

            if (distSq < minDistanceSq) {
                minDistanceSq = distSq
                closestEnemy = enemy
            }
        }

        if (closestEnemy != null) {
            val angle = atan2(
                (closestEnemy.worldY - playerWorldY).toDouble(),
                (closestEnemy.worldX - playerWorldX).toDouble()
            ).toFloat()

            // 🚨 동적으로 계산된 damage와 maxRange를 팩트 규격에 맞춰 던져줍니다.
            val spear = PiercingSpearProjectile(scene, gridBg, playerWorldX, playerWorldY, angle, spearDamage, maxRange)
            scene.world.add(spear, MainScene.Layer.WEAPON)
        }
    }

    override fun draw(canvas: Canvas) {}
}