package kr.ac.tukorea.ge.spgp2026.vampire

import android.view.MotionEvent
import androidx.constraintlayout.helper.widget.Layer
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.HorzScrollBackground
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameView

class MainScene(gctx: GameContext) : Scene(gctx) {
    private lateinit var joyStick: JoyStick
    private lateinit var player: Player

    override fun init() {
        // 1. 조이스틱 먼저 생성 (화면 좌측 하단 고정)
        joyStick = JoyStick(200f, GameView.view.height - 200f) // 좌표는 예시
        add(Layer.UI, joyStick) // UI 레이어에 추가

        // 2. 플레이어 생성 시 조이스틱 참조 전달
        player = Player(GameView.view.width / 2f, GameView.view.height / 2f, joyStick)
        add(Layer.PLAYER, player) // 플레이어 레이어에 추가
    }
}
