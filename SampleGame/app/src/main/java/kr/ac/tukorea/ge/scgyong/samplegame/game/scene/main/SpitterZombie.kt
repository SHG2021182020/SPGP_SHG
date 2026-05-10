package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.*

class SpitterZombie(
    private val gctx: GameContext,
    private val scene: MainScene,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float
) : IGameObject {

    private val sprite = Sprite(gctx, R.mipmap.soccer_ball_240)
    private val speed = 150f
    private val stopDistance = 600f

    private var fireTimer = 0f
    private val fireInterval = 5.0f

    init {
        sprite.width = 100f
        sprite.height = 100f
    }

    override fun update(gctx: GameContext) {
        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        val dx = playerWorldX - worldX
        val dy = playerWorldY - worldY
        val distance = sqrt(dx * dx + dy * dy)
        val angle = atan2(dy.toDouble(), dx.toDouble())

        val dt = 1f / 60f

        if (distance > stopDistance) {
            worldX += (cos(angle) * speed * dt).toFloat()
            worldY += (sin(angle) * speed * dt).toFloat()
        }

        fireTimer += dt
        if (fireTimer >= fireInterval) {
            fireTimer = 0f
            spawnProjectile(angle)
        }

        sprite.x = worldX + gridBg.offsetX
        sprite.y = worldY + gridBg.offsetY
    }

    private fun spawnProjectile(angle: Double) {
        val bullet = EnemyBullet(gctx, gridBg, worldX, worldY, angle)
        scene.world.add(bullet, MainScene.Layer.ENEMY)
    }

    override fun draw(canvas: Canvas) {
        sprite.draw(canvas)
    }
}