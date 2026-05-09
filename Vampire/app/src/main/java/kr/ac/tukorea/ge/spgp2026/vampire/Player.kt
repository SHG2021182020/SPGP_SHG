package kr.ac.tukorea.ge.spgp2026.vampire

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite // AnimSprite 상속 에러를 막기 위해 Sprite로 합성
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameView
import kr.ac.tukorea.ge.spgp2026.dragonflight.R
import kotlin.math.cos
import kotlin.math.sin

// 🚨 엔진의 클래스를 상속(: AnimSprite)받지 말고, 인터페이스(: IGameObject)를 직접 구현하십시오.
class Player(
    gctx: GameContext,
    private val joyStick: JoyStick
) : IGameObject {

    // 🚨 합성을 사용하여 내부에 객체를 소유합니다.
    private val sprite = Sprite(gctx, R.mipmap.fighter)

    private var x: Float = 0f
    private var y: Float = 0f
    private val speed = 400f

    init {
        // 불필요한 null 조건문을 삭제하고 중앙 좌표를 할당합니다.
        x = GameView.view.width / 2f
        y = GameView.view.height / 2f
    }

    override fun update(gctx: GameContext) {
        if (joyStick.power == 0f) return

        val angleRad = joyStick.angle.toDouble()
        val dx = cos(angleRad).toFloat() * joyStick.power
        val dy = sin(angleRad).toFloat() * joyStick.power

        val dt = 1f / 60f

        x += dx * speed * dt
        y += dy * speed * dt
    }

    override fun draw(canvas: Canvas) {
        // 이동한 좌표를 스프라이트 내부 속성에 전달하여 화면에 그립니다.
        // 엔진 버전에 따라 sprite 내부 변수에 직접 접근이 안 되면 별도의 이동 함수를 찾아야 합니다.
        sprite.x = x
        sprite.y = y
        sprite.draw(canvas)
    }
}