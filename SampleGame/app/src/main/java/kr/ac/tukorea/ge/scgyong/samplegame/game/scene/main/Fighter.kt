package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.cos // 🚨 팩트: 조이스틱 각도 계산을 위한 코사인 수학 함수 임포트 필수

class Fighter(
    private val scene: MainScene,
    private val gctx: GameContext,
    private val joyStick: JoyStick
) : IGameObject {

    private val sprite = Sprite(gctx, R.mipmap.character)

    val maxHp = 100
    var hp = maxHp
    var isDead = false

    private var invincibleTimer = 0f
    private val invincibleDuration = 0.5f

    // 🚨 핵심 포인트 1: 플레이어가 마지막으로 바라보던 방향을 기억하는 상태 변수
    private var isFacingLeft = false

    private val healthPaint = Paint().apply { style = Paint.Style.FILL }

    init {
        sprite.width = 100f
        sprite.height = 200f
    }

    override fun update(gctx: GameContext) {
        if (isDead) return

        sprite.x = gctx.metrics.width / 2f
        sprite.y = gctx.metrics.height / 2f

        // 🚨 핵심 포인트 2: 조이스틱이 움직일 때만 방향 갱신 (입력이 0이면 이전 시선 유지)
        if (joyStick.power > 0) {
            // 수학적 팩트: 라디안 각도의 코사인 값이 음수이면 왼쪽을 향하고 있는 것입니다.
            isFacingLeft = cos(joyStick.angle) < 0
        }

        if (invincibleTimer > 0f) {
            val dt = 1f / 60f
            invincibleTimer -= dt
        }
    }

    fun takeDamage(amount: Int) {
        if (isDead || invincibleTimer > 0f) return

        hp -= amount
        invincibleTimer = invincibleDuration

        if (hp <= 0) {
            hp = 0
            isDead = true
        }
    }

    override fun draw(canvas: Canvas) {
        if (isDead) return

        val isBlinking = invincibleTimer > 0f && ((invincibleTimer * 60).toInt() % 10 < 5)

        if (!isBlinking) {
            // 🚨 핵심 포인트 3: 스프라이트 렌더링에만 좌우 반전 매트릭스를 적용하고 즉시 복구합니다.
            canvas.save()
            if (isFacingLeft) {
                // 스프라이트의 X,Y 중심점을 축으로 캔버스의 X축만 반대로 뒤집습니다.
                canvas.scale(-1f, 1f, sprite.x, sprite.y)
            }
            sprite.draw(canvas)
            canvas.restore() // 캔버스를 원상태로 돌려 체력바가 뒤집히는 것을 방지
        }

        val barWidth = 100f
        val barHeight = 12f
        val cx = gctx.metrics.width / 2f
        val cy = gctx.metrics.height / 2f + 80f

        val left = cx - barWidth / 2f
        val top = cy - barHeight / 2f

        healthPaint.color = Color.RED
        canvas.drawRect(left, top, left + barWidth, top + barHeight, healthPaint)

        val hpRatio = hp.toFloat() / maxHp.toFloat()
        healthPaint.color = Color.GREEN
        canvas.drawRect(left, top, left + (barWidth * hpRatio), top + barHeight, healthPaint)
    }

    fun addExp(amount: Int) {
        scene.levelManager.addExp(amount)
    }
}