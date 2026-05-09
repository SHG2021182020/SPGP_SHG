package kr.ac.tukorea.ge.spgp2026.vampire

import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class PixelSurvivalActivity : BaseGameActivity() {
    // 🚨 createScene을 createRootScene으로 정확히 변경해야 합니다.
    override fun createRootScene(gctx: GameContext): Scene {
        return MainScene(gctx)
    }
}