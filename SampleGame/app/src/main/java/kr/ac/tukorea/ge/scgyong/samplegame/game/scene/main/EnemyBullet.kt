package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.cos
import kotlin.math.sin

class EnemyBullet(
    gctx: GameContext,
    private val gridBg: GridBackground,
    private var worldX: Float,
    private var worldY: Float,
    private val angle: Double
) : IGameObject {

    private val sprite = Sprite(gctx, R.mipmap.soccer_ball_240)
    private val speed = 500f

    init {
        sprite.width = 40f
        sprite.height = 40f
    }

    override fun update(gctx: GameContext) {
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