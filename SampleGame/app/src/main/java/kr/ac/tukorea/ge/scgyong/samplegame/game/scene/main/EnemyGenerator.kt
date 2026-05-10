package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class EnemyGenerator(
    private val scene: MainScene,
    private val gridBg: GridBackground
) : IGameObject {

    private var spawnTimer = 0f
    private val spawnInterval = 1.0f

    override fun update(gctx: GameContext) {
        val dt = 1f / 60f
        spawnTimer += dt

        if (spawnTimer >= spawnInterval) {
            spawnTimer -= spawnInterval
            spawnZombie(gctx)
        }
    }

    private fun spawnZombie(gctx: GameContext) {
        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        val angle = Random.nextDouble(0.0, 2 * Math.PI)
        val spawnRadius = 1500f

        val spawnX = playerWorldX + (cos(angle) * spawnRadius).toFloat()
        val spawnY = playerWorldY + (sin(angle) * spawnRadius).toFloat()

        val rand = Random.nextFloat()
        val enemy: IGameObject = when {
            rand < 0.1f -> SpitterZombie(gctx, scene, gridBg, spawnX, spawnY) // 20% 원거리
            rand < 0.2f -> ChargerZombie(gctx, gridBg, spawnX, spawnY)        // 30% 돌진
            else -> Zombie(gctx, gridBg, spawnX, spawnY)                      // 50% 일반
        }

        scene.world.add(enemy, MainScene.Layer.ENEMY)
    }

    override fun draw(canvas: Canvas) {
    }
}