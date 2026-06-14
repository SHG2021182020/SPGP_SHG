package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kr.ac.tukorea.ge.scgyong.samplegame.R
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kotlin.system.exitProcess

class MainScene(gctx: GameContext) : Scene(gctx) {
    enum class Layer { BG, ITEM, WEAPON_UNDER, ENEMY, PLAYER, WEAPON, UI }

    private val screenW = gctx.metrics.width.toFloat()
    private val screenH = gctx.metrics.height.toFloat()
    val enemies = mutableListOf<IEnemy>()

    private val joyStick = JoyStick(
        gctx = gctx, bgResId = R.mipmap.joystick_bg, thumbResId = R.mipmap.joystick_thumb,
        centerX = 250f, centerY = -250f, bgRadius = 150f, thumbRadius = 50f
    )

    private val gridBg = GridBackground(gctx, joyStick)
    val player = Fighter(this, gctx, joyStick)
    private val enemyGenerator = EnemyGenerator(this, gridBg)
    val levelManager = LevelManager(this)

    private lateinit var autoGun: AutoGun
    private lateinit var garlic: Garlic
    private lateinit var piercingSpear: PiercingSpearWeapon

    var isUiOverlayActive = false
    private var activeOptions = listOf<UpgradeOption>()
    private val uiPaint = Paint().apply { isAntiAlias = true }

    var playTime = 0f
    var isGameOver = false
    var killCount = 0

    private var resultUi: ResultUi? = null

    // 🚨 텍스트 크기를 60f로 줄여 상단 배치 시 가독성을 높입니다.
    private val timePaint = Paint().apply { color = Color.WHITE; textSize = 60f; textAlign = Paint.Align.CENTER; isAntiAlias = true; isFakeBoldText = true }
    private val timeStrokePaint = Paint().apply { color = Color.BLACK; textSize = 60f; textAlign = Paint.Align.CENTER; isAntiAlias = true; isFakeBoldText = true; style = Paint.Style.STROKE; strokeWidth = 6f }

    private val expBgPaint = Paint().apply { color = Color.parseColor("#66000000"); style = Paint.Style.FILL }
    private val expBarPaint = Paint().apply { color = Color.parseColor("#00A2FF"); style = Paint.Style.FILL }

    override val world = World(Layer.values()).apply {
        add(gridBg, Layer.BG); add(enemyGenerator, Layer.BG); add(player, Layer.PLAYER)
        garlic = Garlic(this@MainScene, gridBg)
        autoGun = AutoGun(this@MainScene, gridBg)
        piercingSpear = PiercingSpearWeapon(this@MainScene, gridBg)
        add(garlic, Layer.WEAPON); add(autoGun, Layer.WEAPON); add(piercingSpear, Layer.WEAPON)
        add(joyStick, Layer.UI)
    }

    fun showUpgradeUi() {
        isUiOverlayActive = true
        activeOptions = UpgradeOption.getRandomOptions()
    }

    private fun selectUpgrade(option: UpgradeOption) {
        when (option) {
            UpgradeOption.HEAL -> {
                player.hp = (player.hp + (player.maxHp / 2)).coerceAtMost(player.maxHp)
            }
            UpgradeOption.AUTOGUN_DAMAGE, UpgradeOption.AUTOGUN_DELAY -> {
                autoGun.weaponLevel++
            }
            UpgradeOption.GARLIC_RANGE, UpgradeOption.GARLIC_DELAY -> {
                garlic.weaponLevel++
            }
            UpgradeOption.SPEAR_DAMAGE, UpgradeOption.SPEAR_RANGE, UpgradeOption.SPEAR_DELAY -> {
                piercingSpear.weaponLevel++
            }
        }
        isUiOverlayActive = false
    }

    override fun update(gctx: GameContext) {
        if (isGameOver || isUiOverlayActive) return

        super.update(gctx)
        val dt = 1f / 60f
        playTime += dt

        if (player.isDead || playTime >= 600f) {
            isGameOver = true
            resultUi = ResultUi(
                screenW, screenH, player.isDead, playTime, levelManager.level, killCount,
                onRestart = { restartGame() },
                onQuit = { quitGame() }
            )
            return
        }

        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            if (enemy.isDead) {
                world.remove(enemy as IGameObject, Layer.ENEMY)
                iterator.remove()
                killCount++
            }
        }

        if (!player.isDead) {
            val playerWorldX = -gridBg.offsetX + screenW / 2f
            val playerWorldY = -gridBg.offsetY + screenH / 2f
            val hitRadiusSq = 40f * 40f

            for (enemy in enemies) {
                if (enemy.isDead) continue
                val dx = enemy.worldX - playerWorldX
                val dy = enemy.worldY - playerWorldY
                if (dx * dx + dy * dy <= hitRadiusSq) {
                    player.takeDamage(10)
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (isGameOver) {
            resultUi?.draw(canvas)
            return
        }

        // 1. 경험치 바 렌더링 (두께를 40f로 증가시켜 시인성 확보)
        val expBarHeight = 40f
        canvas.drawRect(0f, 0f, screenW, expBarHeight, expBgPaint)

        val currentExp = levelManager.currentExp.toFloat()
        val maxExp = levelManager.expToNextLevel.toFloat()
        val expRatio = (currentExp / maxExp).coerceIn(0f, 1f)
        canvas.drawRect(0f, 0f, screenW * expRatio, expBarHeight, expBarPaint)

        // 2. 플레이타임 텍스트 렌더링
        val minutes = (playTime / 60).toInt()
        val seconds = (playTime % 60).toInt()
        val timeString = "%02d:%02d".format(minutes, seconds)

        // 🚨 팩트: 하드코딩된 위치를 버리고, 경험치 바 바로 밑동(110f)으로 동기화 결합
        val textX = screenW / 2f
        val textY = expBarHeight + 70f

        canvas.drawText(timeString, textX, textY, timeStrokePaint)
        canvas.drawText(timeString, textX, textY, timePaint)

        // 3. 업그레이드 선택 UI 렌더링
        if (isUiOverlayActive) {
            canvas.drawRect(0f, 0f, screenW, screenH, uiPaint.apply { color = Color.parseColor("#AA000000") })
            val cardHeight = screenH / 5f
            for (i in activeOptions.indices) {
                val top = (screenH / 4f) + (i * (cardHeight + 40f))
                canvas.drawRect(screenW * 0.1f, top, screenW * 0.9f, top + cardHeight, uiPaint.apply { color = Color.DKGRAY })
                uiPaint.color = Color.WHITE; uiPaint.textSize = 50f
                canvas.drawText(activeOptions[i].title, screenW * 0.15f, top + 70f, uiPaint)
                uiPaint.color = Color.LTGRAY; uiPaint.textSize = 35f
                canvas.drawText(activeOptions[i].description, screenW * 0.15f, top + 140f, uiPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isGameOver) {
            resultUi?.onTouchEvent(event)
            return true
        }

        if (isUiOverlayActive) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                // 🚨 팩트: 엔진이 전달하는 event.x와 y는 이미 변환된 상태이므로 순수값을 사용해야 터치가 정확히 꽂힙니다.
                val ex = event.x
                val ey = event.y

                val cardHeight = screenH / 5f
                for (i in activeOptions.indices) {
                    val top = (screenH / 4f) + (i * (cardHeight + 40f))
                    if (ex >= screenW * 0.1f && ex <= screenW * 0.9f && ey >= top && ey <= top + cardHeight) {
                        selectUpgrade(activeOptions[i])
                        return true
                    }
                }
            }
            return true
        }

        joyStick.onTouchEvent(event)
        return true
    }

    private fun restartGame() {
        MainScene(gctx).change()
    }

    private fun quitGame() {
        // 🚨 팩트: 앱이 재시작되는 크래시를 막기 위해, GameContext에서 Activity를 추출해 안전하게 종료시킵니다.
        try {
            var ctx = (gctx as? View)?.context ?: (gctx as? ContextWrapper)?.baseContext
            while (ctx is ContextWrapper) {
                if (ctx is Activity) {
                    ctx.finishAffinity() // 완벽한 안드로이드 생명주기 종료
                    return
                }
                ctx = ctx.baseContext
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 최후의 예외 처리
        exitProcess(0)
    }
}