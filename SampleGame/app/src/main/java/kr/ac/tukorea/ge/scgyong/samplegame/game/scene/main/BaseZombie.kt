package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R

abstract class BaseZombie(
    protected val gctx: GameContext,
    protected val scene: MainScene,
    protected val gridBg: GridBackground,
    override var worldX: Float,
    override var worldY: Float,
    resId: Int = R.mipmap.soccer_ball_240,
    width: Float,
    height: Float,
    protected var hp: Int = 10
) : IGameObject, IEnemy {

    protected val sprite = Sprite(gctx, resId)
    protected var maxHp: Int = hp
    override var isDead: Boolean = false

    // 🚨 핵심 포인트 1: 부모 클래스에 좀비의 시선을 캐싱하는 변수 선언
    protected var isFacingLeft = false

    private val healthPaint = Paint().apply { style = Paint.Style.FILL }

    init {
        sprite.width = width
        sprite.height = height

        val timeMinutes = scene.playTime / 60f
        val healthMultiplier = 1f + (timeMinutes * 0.5f)
        val baseHp = 10

        maxHp = (baseHp * healthMultiplier).toInt()
        hp = maxHp
    }

    override fun takeDamage(amount: Int) {
        if (isDead) return
        hp -= amount
        if (hp <= 0) {
            die()
        }
    }

    protected open fun die() {
        isDead = true
        scene.world.add(ExpOrb(scene, gridBg, worldX, worldY), MainScene.Layer.ITEM)
    }

    abstract fun updateAI(dt: Float, playerWorldX: Float, playerWorldY: Float)

    override fun update(gctx: GameContext) {
        val dt = 1f / 60f

        val playerWorldX = -gridBg.offsetX + gctx.metrics.width / 2f
        val playerWorldY = -gridBg.offsetY + gctx.metrics.height / 2f

        // 🚨 핵심 포인트 2: 플레이어의 X 좌표가 나의 X 좌표보다 작으면(왼쪽이면) true
        isFacingLeft = playerWorldX < worldX

        updateAI(dt, playerWorldX, playerWorldY)

        sprite.x = worldX + gridBg.offsetX
        sprite.y = worldY + gridBg.offsetY
    }

    override fun draw(canvas: Canvas) {
        // 🚨 핵심 포인트 3: 좀비 이미지만 좌우 반전 매트릭스 적용
        canvas.save()
        if (isFacingLeft) {
            canvas.scale(-1f, 1f, sprite.x, sprite.y)
        }
        sprite.draw(canvas)
        canvas.restore() // 캔버스를 복구하여 아래의 체력바가 뒤집히는 것을 방어

        val barWidth = 80f
        val barHeight = 10f
        val yOffset = sprite.height / 2f + 20f

        val left = sprite.x - barWidth / 2f
        val top = sprite.y - yOffset - barHeight
        val right = sprite.x + barWidth / 2f
        val bottom = sprite.y - yOffset

        healthPaint.color = Color.RED
        canvas.drawRect(left, top, right, bottom, healthPaint)

        val currentHp = hp.coerceAtLeast(0)
        val hpRatio = currentHp.toFloat() / maxHp.toFloat()
        val hpRight = left + (barWidth * hpRatio)

        healthPaint.color = Color.GREEN
        canvas.drawRect(left, top, hpRight, bottom, healthPaint)
    }
}