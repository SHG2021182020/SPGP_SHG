package kr.ac.tukorea.ge.spgp2026.vampire

import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameView
// 1번의 해결로 R 클래스가 정상 생성되겠지만, 확실히 하기 위해 명시적 임포트
import kr.ac.tukorea.ge.spgp2026.vampire.R

class MainScene(gctx: GameContext) : Scene(gctx) {

    enum class Layer {
        BG, ENEMY, ITEM, WEAPON, PLAYER, UI
    }

    private lateinit var joyStick: JoyStick
    private lateinit var player: Player

    // 🚨 override fun appendObjects() 같은 없는 함수를 쓰지 말고 init 블록을 사용합니다.
    init {
        // GameContext에 height가 없을 수 있으므로 GameView에서 안전하게 화면 크기를 가져옵니다.
        val viewWidth = GameView.view?.width?.toFloat() ?: 1000f
        val viewHeight = GameView.view?.height?.toFloat() ?: 2000f

        joyStick = JoyStick(
            gctx,
            centerX = 200f,
            centerY = viewHeight - 200f,
            bgRadius = 150f,
            thumbRadius = 50f,
            bgResId = R.mipmap.tu_joystick_bg,
            thumbResId = R.mipmap.tu_joystick_thumb
        )

        // Enum의 순서값(Int)을 사용하여 레이어에 객체를 추가합니다.
        add(Layer.UI.ordinal, joyStick)

        player = Player(gctx, joyStick)
        add(Layer.PLAYER.ordinal, player)
    }
}