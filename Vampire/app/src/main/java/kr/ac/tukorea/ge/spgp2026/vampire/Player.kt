package kr.ac.tukorea.ge.spgp2026.vampire

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameView
import kotlin.math.cos
import kotlin.math.sin

class Player(
    gctx: GameContext,
    private val joyStick: JoyStick
) : IGameObject {

    // 1. 합성을 통해 내부에 스프라이트 객체를 생성합니다.
    // 기존의 width, height 파라미터는 삭제하고 gctx와 리소스ID만 넘깁니다.
    private val sprite = Sprite(gctx, R.mipmap.fighter)

    // 플레이어의 실제 좌표
    private var x: Float = 0f
    private var y: Float = 0f
    private val speed = 400f

    init {
        // 화면 크기를 GameView에서 가져와 플레이어를 중앙에 배치합니다.
        val view = GameView.view
        if (view != null) {
            x = view.width / 2f
            y = view.height / 2f

            // 스프라이트의 좌표 동기화 (엔진의 속성 접근 방식에 따라 달라질 수 있음)
            // 엔진이 x, y 변수를 제공한다면:
            // sprite.x = x
            // sprite.y = y
        }
    }

    override fun update(gctx: GameContext) {
        // 조이스틱 입력이 없으면 이동하지 않음
        if (joyStick.power == 0f) return

        // 방향 벡터 계산
        val angleRad = joyStick.angle.toDouble()
        val dx = cos(angleRad).toFloat() * joyStick.power
        val dy = sin(angleRad).toFloat() * joyStick.power

        // GameContext 내부에 dt가 없다면, 60fps 고정으로 dt를 하드코딩하여 에러를 막습니다.
        val dt = 1f / 60f

        // 이동 연산
        x += dx * speed * dt
        y += dy * speed * dt

        // 이동한 좌표를 내부 스프라이트에 적용
        // 만약 sprite.x, sprite.y 로 접근이 안되면 sprite.moveTo(x, y) 등을 사용하세요.
        // sprite.x = x
        // sprite.y = y
    }

    override fun draw(canvas: Canvas) {
        // 나를 그릴 타이밍에 내부의 sprite에게 그리기를 위임합니다.
        sprite.draw(canvas)
    }
}