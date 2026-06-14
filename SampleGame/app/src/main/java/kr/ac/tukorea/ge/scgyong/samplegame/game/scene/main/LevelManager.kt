package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

class LevelManager(private val scene: MainScene) {
    var level = 1
    var currentExp = 0
    var expToNextLevel = 100

    fun addExp(amount: Int) {
        currentExp += amount
        if (currentExp >= expToNextLevel) {
            levelUp()
        }
    }

    private fun levelUp() {
        currentExp -= expToNextLevel
        level++
        expToNextLevel = (expToNextLevel * 1.2f).toInt()

        // 🚨 핵심: 레벨업 발생 시 씬을 정지 상태로 만들고 무작위 업그레이드 선택지 활성화!
        scene.showUpgradeUi()
    }
}