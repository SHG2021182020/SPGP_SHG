package kr.ac.tukorea.ge.spgp2026.vampire

import kr.ac.tukorea.ge.spgp2026.a2dg.objects.AnimSprite
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kotlin.math.cos
import kotlin.math.sin

class Player(
    x: Float,
    y: Float,
    private val joyStick: JoyStick, fps: Float
) : AnimSprite(x, y, fps) {

    // 플레이어의 이동 속도 (픽셀/초 단위)
    private var speed = 400f

    override fun update(gctx: Float) { // a2dg 엔진의 프레임 업데이트 주기
        super.update(gctx)

        // 1. 조이스틱의 입력이 없을 때는 이동 연산 스킵
        if (joyStick.power == 0f) return

        // 2. 조이스틱의 각도(angle)와 힘(power)을 X, Y 벡터로 변환
        // 조이스틱의 max power가 1.0이라고 가정
        val dx = cos(joyStick.angle.toDouble()).toFloat() * joyStick.power
        val dy = sin(joyStick.angle.toDouble()).toFloat() * joyStick.power

        // 3. 현재 위치 갱신 (반드시 dt를 곱해야 함)
        x += dx * speed * gctx
        y += dy * speed * gctx

        // 4. 이동 방향에 따른 스프라이트 좌우 반전 (필요시)
        if (dx < 0) {
            // 왼쪽을 볼 때의 이미지 플립 로직 (예: srcRect 뒤집기 등 a2dg 방식 적용)
        } else if (dx > 0) {
            // 오른쪽을 볼 때의 이미지 복구 로직
        }
    }
}