package kr.ac.tukorea.ge.spgp2026.vampire

import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene

class PixelSurvivalActivity : BaseGameActivity() {
    override val scene: Scene
        get() = MainScene() // 방금 전 조이스틱과 플레이어를 구현한 씬을 반환
}