package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin

class GridBackground(
    private val gctx: GameContext,
    private val joyStick: JoyStick
) : IGameObject {
    private val paint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 2f
    }
    private val gridSize = 100f

    var offsetX = 0f
    var offsetY = 0f
    private val speed = 400f

    override fun update(gctx: GameContext) {
        if (joyStick.power == 0f) return

        val angle = joyStick.angle.toDouble()
        val dx = cos(angle).toFloat() * joyStick.power
        val dy = sin(angle).toFloat() * joyStick.power
        val dt = 1f / 60f

        offsetX -= dx * speed * dt
        offsetY -= dy * speed * dt
    }

    override fun draw(canvas: Canvas) {
        val startX = offsetX % gridSize
        val startY = offsetY % gridSize

        var x = startX - gridSize
        while (x < gctx.metrics.width + gridSize) {
            canvas.drawLine(x, 0f, x, gctx.metrics.height.toFloat(), paint)
            x += gridSize
        }

        var y = startY - gridSize
        while (y < gctx.metrics.height + gridSize) {
            canvas.drawLine(0f, y, gctx.metrics.width.toFloat(), y, paint)
            y += gridSize
        }
    }
}