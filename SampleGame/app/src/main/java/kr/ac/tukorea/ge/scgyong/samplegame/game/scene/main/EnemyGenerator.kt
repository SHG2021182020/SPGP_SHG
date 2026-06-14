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

        val angle = kotlin.random.Random.nextDouble(0.0, 2 * Math.PI)
        val spawnRadius = 1500f

        val spawnX = playerWorldX + (cos(angle) * spawnRadius).toFloat()
        val spawnY = playerWorldY + (sin(angle) * spawnRadius).toFloat()

        val rand = kotlin.random.Random.nextFloat()
        val enemy: IGameObject = when {
            rand < 0.1f -> SpitterZombie(gctx, scene, gridBg, spawnX, spawnY) // this가 아니라 scene!
            rand < 0.2f -> ChargerZombie(gctx, scene, gridBg, spawnX, spawnY) // scene 파라미터 추가
            else -> Zombie(gctx, scene, gridBg, spawnX, spawnY)               // scene 파라미터 추가
        }

        scene.world.add(enemy, MainScene.Layer.ENEMY)

        // 주의: 오토 타겟팅 무기가 적을 인식하려면 아래 코드도 반드시 추가해야 해!
        scene.enemies.add(enemy as IEnemy)
    }

    override fun draw(canvas: Canvas) {
    }
}