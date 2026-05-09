package kr.ac.tukorea.ge.spgp2026.a2dg.objects

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class HorzScrollBackground(
    gctx: GameContext,
    resId: Int,
    private val speed: Float,
) : Sprite(gctx, resId) {
    private val screenWidth = gctx.metrics.width
    private val screenHeight = gctx.metrics.height

    private val tileHeight = bitmapHeight * screenWidth / bitmapWidth.toFloat()

    init {
        x = screenWidth / 2f
        y = 0f
    }

    override fun update(gctx: GameContext) {
        y += speed * gctx.frameTime
    }

    override fun draw(canvas: Canvas) {
        var curr = y % tileHeight

        if (curr > 0f) curr -= tileHeight

        while (curr < screenHeight) {
            dstRect.set(0f, curr, screenWidth.toFloat(), curr + tileHeight)
            canvas.drawBitmap(bitmap, null, dstRect, null)
            curr += tileHeight
        }
    }
}