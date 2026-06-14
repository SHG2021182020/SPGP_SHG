package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import kr.ac.tukorea.ge.spgp2026.a2dg.R
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.*

class SpitterZombie(
    gctx: GameContext, scene: MainScene, gridBg: GridBackground, worldX: Float, worldY: Float
) : BaseZombie(
    gctx, scene, gridBg, worldX, worldY,
    resId = kr.ac.tukorea.ge.scgyong.samplegame.R.mipmap.spitter, // 🚨 핵심: 부모에게 지팡이 몬스터 이미지를 전달
    width = 100f, height = 200f, hp = 15
) {

    private val speed = 150f
    private val stopDistance = 600f
    private var fireTimer = 0f
    private val fireInterval = 5.0f

    override fun updateAI(dt: Float, playerWorldX: Float, playerWorldY: Float) {
        if (scene.isUiOverlayActive) return
        val dx = playerWorldX - worldX
        val dy = playerWorldY - worldY
        val distance = sqrt(dx * dx + dy * dy)
        val angle = atan2(dy.toDouble(), dx.toDouble())

        if (distance > stopDistance) {
            worldX += (cos(angle) * speed * dt).toFloat()
            worldY += (sin(angle) * speed * dt).toFloat()
        }

        fireTimer += dt
        if (fireTimer >= fireInterval) {
            fireTimer = 0f
            spawnProjectile(angle)
        }
    }

    private fun spawnProjectile(angle: Double) {
        // 🚨 허점 해결: EnemyBullet 생성자의 변경된 시그니처에 맞춰 첫 번째 인자로 scene을 주입합니다.
        val bullet = EnemyBullet(scene, gctx, gridBg, worldX, worldY, angle.toFloat())

        // 투사체는 적과 레이어를 분리하여 WEAPON 레이어 등으로 관리하는 것이 일반적이나,
        // 기존 설계를 존중하여 ENEMY 레이어에 추가합니다.
        scene.world.add(bullet, MainScene.Layer.ENEMY)
    }
}