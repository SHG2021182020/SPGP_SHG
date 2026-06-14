package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kotlin.math.cos
import kotlin.math.sin

class GridBackground(
    private val gctx: GameContext,
    private val joyStick: JoyStick
) : IGameObject {

    var offsetX = 0f
    var offsetY = 0f

    private val mapSprite = Sprite(gctx, R.mipmap.map_bg)

    override fun update(gctx: GameContext) {
        val dt = 1f / 60f
        val speed = 300f * dt

        if (joyStick.power > 0) {
            val dx = cos(joyStick.angle.toDouble()).toFloat() * speed * joyStick.power
            val dy = sin(joyStick.angle.toDouble()).toFloat() * speed * joyStick.power
            offsetX -= dx
            offsetY -= dy
        }
    }

    override fun draw(canvas: Canvas) {
        val screenW = gctx.metrics.width.toFloat()
        val screenH = gctx.metrics.height.toFloat()

        val tileSize = 1500f

        // 🚨 팩트: 부동소수점 오차로 인한 타일 사이의 미세한 흰색 틈(Seam)을 완벽히 박멸하기 위해
        // 시각적으로 그려지는 이미지의 크기를 타일 간격보다 2픽셀씩 더 늘려 살짝 겹치도록(Overlap) 렌더링합니다.
        mapSprite.width = tileSize
        mapSprite.height = tileSize + 500f

        // 🚨 핵심 수학 팩트: 음수 좌표계에서도 타일 시작점이 절대 흔들리지 않도록 양수 모듈러(Positive Modulo)를 적용합니다.
        val shiftX = ((offsetX % tileSize) + tileSize) % tileSize
        val shiftY = ((offsetY % tileSize) + tileSize) % tileSize

        // 항상 화면 왼쪽 위 바깥(-tileSize ~ 0 사이)에서 타일링이 시작되도록 절대 고정
        val startX = shiftX - tileSize
        val startY = shiftY - tileSize

        // 렌더링 범위를 1타일 여유 있게 잡아 화면 바깥쪽을 완전히 덮습니다.
        var cy = startY
        while (cy < screenH + tileSize) {
            var cx = startX
            while (cx < screenW + tileSize) {
                // 논리적 간격은 여전히 정확하게 tileSize(500f) 단위로 전진시킵니다.
                mapSprite.x = cx + tileSize / 2f
                mapSprite.y = cy + tileSize / 2f
                mapSprite.draw(canvas)

                cx += tileSize
            }
            cy += tileSize
        }
    }
}