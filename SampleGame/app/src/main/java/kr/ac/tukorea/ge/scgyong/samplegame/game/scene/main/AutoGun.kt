package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main.PlayerBullet
import kotlin.math.atan2

class AutoGun(
    private val scene: MainScene, // 🚨 이 줄이 반드시 있어야 합니다!
    private val gridBg: GridBackground
) : IGameObject {

    var weaponLevel = 1 // 🚨 외부 개방 레벨

    private var fireTimer = 0f

    // 레벨에 따른 가변 스탯 구조화
    val bulletDamage: Int get() = 1 + (weaponLevel * 4)       // 레벨당 데미지 4 증가
    private val fireRate: Float get() = maxOf(0.15f, 2.0f - (weaponLevel * 0.05f)) // 레벨당 선딜 0.05초 감소 (최대 연사 0.15초)
    private val rangeSq = 800f * 800f

    override fun update(gctx: GameContext) {
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
        var minDistanceSq = rangeSq

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

            // 총알 생성 시 AutoGun의 현재 가변 데미지 데이터를 실시간 주입(Dependency Injection)
            val bullet = PlayerBullet(scene, gridBg, playerWorldX, playerWorldY, angle, bulletDamage)
            scene.world.add(bullet, MainScene.Layer.WEAPON)
        }
    }

    override fun draw(canvas: Canvas) {}
}