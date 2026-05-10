package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.PI

class Fighter(
    private val gctx: GameContext,
    private val joyStick: JoyStick
) : IGameObject {

    private val sprite = Sprite(gctx, R.mipmap.plane_240)
    private var isFlipped = false

    init {
        sprite.width = 150f
        sprite.height = 150f
        sprite.x = gctx.metrics.width / 2f
        sprite.y = gctx.metrics.height / 2f
    }

    override fun update(gctx: GameContext) {
        if (joyStick.power > 0) {
            val angle = joyStick.angle
            isFlipped = angle > PI / 2 || angle < -PI / 2
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        if (isFlipped) {
            canvas.scale(-1f, 1f, sprite.x, sprite.y)
        }
        sprite.draw(canvas)
        canvas.restore()
    }
}