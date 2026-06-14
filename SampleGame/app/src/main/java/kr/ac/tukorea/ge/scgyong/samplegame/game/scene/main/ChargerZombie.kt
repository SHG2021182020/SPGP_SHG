package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ChargerZombie(
    gctx: GameContext, scene: MainScene, gridBg: GridBackground, worldX: Float, worldY: Float
) : BaseZombie(
    gctx, scene, gridBg, worldX, worldY,
    resId = kr.ac.tukorea.ge.scgyong.samplegame.R.mipmap.tank, // 🚨 핵심: 부모에게 낫 몬스터 이미지를 전달
    width = 150f, height = 150f, hp = 20
) {

    private var state = 0
    private var stateTimer = 0f
    private val normalSpeed = 0f
    private val dashSpeed = 1000f
    private var dashAngle = 0.0

    override fun updateAI(dt: Float, playerWorldX: Float, playerWorldY: Float) {
        if (scene.isUiOverlayActive) return
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
    }
}