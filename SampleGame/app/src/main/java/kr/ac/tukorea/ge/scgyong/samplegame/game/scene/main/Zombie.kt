package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import kr.ac.tukorea.ge.scgyong.samplegame.R
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Zombie(
    gctx: GameContext, scene: MainScene, gridBg: GridBackground, worldX: Float, worldY: Float
) : BaseZombie(
    gctx, scene, gridBg, worldX, worldY,
    resId = R.mipmap.zombie, // 🚨 일반 몬스터용 이미지가 있다면 이곳에 전달
    width = 100f, height = 200f, hp = 10
) {

    private val speed = 100f

    override fun updateAI(dt: Float, playerWorldX: Float, playerWorldY: Float) {
        if (scene.isUiOverlayActive) return
        val dx = playerWorldX - worldX
        val dy = playerWorldY - worldY
        val angle = atan2(dy.toDouble(), dx.toDouble())

        worldX += (cos(angle) * speed * dt).toFloat()
        worldY += (sin(angle) * speed * dt).toFloat()
    }
}