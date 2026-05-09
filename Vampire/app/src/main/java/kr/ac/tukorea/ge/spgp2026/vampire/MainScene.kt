package kr.ac.tukorea.ge.spgp2026.vampire

import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameView
import kr.ac.tukorea.ge.spgp2026.dragonflight.R

class MainScene(gctx: GameContext) : Scene(gctx) {

    enum class Layer {
        BG, ENEMY, ITEM, WEAPON, PLAYER, UI
    }

    // lateinit의 불필요한 남발을 막고 클래스 생성 시점에 즉시 할당합니다.
    // GameView.view는 null이 아니므로 안전 호출(?.)을 제거했습니다.
    private val joyStick = JoyStick(
        gctx,
        centerX = 200f,
        centerY = GameView.view.height.toFloat() - 200f,
        bgRadius = 150f,
        thumbRadius = 50f,
        bgResId = R.mipmap.tu_joystick_bg,
        thumbResId = R.mipmap.tu_joystick_thumb
    )

    private val player = Player(gctx, joyStick)

    init {
        // Enum의 ordinal 값을 정확히 추출하여 add 에러를 방지합니다.
        add(Layer.UI.ordinal, joyStick)
        add(Layer.PLAYER.ordinal, player)
    }
}