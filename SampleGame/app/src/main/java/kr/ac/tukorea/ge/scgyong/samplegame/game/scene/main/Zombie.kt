package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Zombie(
    private val gctx: GameContext,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float
) : IGameObject {

    private val sprite = Sprite(gctx, R.mipmap.soccer_ball_240)
    private val speed = 100f

    init {
        sprite.width = 120f
        sprite.height = 120f
    }

    override fun update(gctx: GameContext) {
        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        val dx = playerWorldX - worldX
        val dy = playerWorldY - worldY
        val angle = atan2(dy.toDouble(), dx.toDouble())

        val dt = 1f / 60f
        worldX += (cos(angle) * speed * dt).toFloat()
        worldY += (sin(angle) * speed * dt).toFloat()

        sprite.x = worldX + gridBg.offsetX
        sprite.y = worldY + gridBg.offsetY
    }

    override fun draw(canvas: Canvas) {
        sprite.draw(canvas)
    }
}