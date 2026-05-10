package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R

class MainScene(gctx: GameContext) : Scene(gctx) {
    enum class Layer {
        BG, ENEMY, ITEM, WEAPON, PLAYER, UI
    }

    private val joyStick = JoyStick(
        gctx,
        centerX = gctx.metrics.width / 2f,
        centerY = gctx.metrics.height - 300f,
        bgRadius = 150f,
        thumbRadius = 50f,
        bgResId = R.mipmap.joystick_bg,
        thumbResId = R.mipmap.joystick_thumb
    )

    private val gridBg = GridBackground(gctx, joyStick)
    private val player = Fighter(gctx, joyStick)
    private val enemyGenerator = EnemyGenerator(this, gridBg)

    override val world = World(Layer.entries.toTypedArray()).apply {
        add(gridBg, Layer.BG)
        add(enemyGenerator, Layer.BG)
        add(player, Layer.PLAYER)
        add(joyStick, Layer.UI)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (joyStick.onTouchEvent(event)) {
            return true
        }
        return false
    }
}