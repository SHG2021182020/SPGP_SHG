package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ChargerZombie(
    private val gctx: GameContext,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float
) : IGameObject {

    private val sprite = Sprite(gctx, R.mipmap.soccer_ball_240)

    private var state = 0
    private var stateTimer = 0f

    private val normalSpeed = 00f
    private val dashSpeed = 1000f
    private var dashAngle = 0.0

    init {
        sprite.width = 200f
        sprite.height = 200f
    }

    override fun update(gctx: GameContext) {
        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        val dt = 1f / 60f
        stateTimer += dt

        when (state) {
            0 -> {
                val dx = playerWorldX - worldX
                val dy = playerWorldY - worldY
                val angle = atan2(dy.toDouble(), dx.toDouble())

                worldX += (cos(angle) * normalSpeed * dt).toFloat()
                worldY += (sin(angle) * normalSpeed * dt).toFloat()

                if (stateTimer >= 3f) {
                    state = 1
                    stateTimer = 0f
                }
            }
            1 -> {
                val dx = playerWorldX - worldX
                val dy = playerWorldY - worldY
                dashAngle = atan2(dy.toDouble(), dx.toDouble())

                if (stateTimer >= 1f) {
                    state = 2
                    stateTimer = 0f
                }
            }
            2 -> {
                worldX += (cos(dashAngle) * dashSpeed * dt).toFloat()
                worldY += (sin(dashAngle) * dashSpeed * dt).toFloat()

                if (stateTimer >= 1.0f) {
                    state = 3
                    stateTimer = 0f
                }
            }
            3 -> {
                if (stateTimer >= 2.0f) {
                    state = 0
                    stateTimer = 0f
                }
            }
        }

        sprite.x = worldX + gridBg.offsetX
        sprite.y = worldY + gridBg.offsetY
    }

    override fun draw(canvas: Canvas) {
        sprite.draw(canvas)
    }
}