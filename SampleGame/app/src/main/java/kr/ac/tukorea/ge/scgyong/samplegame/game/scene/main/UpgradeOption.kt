package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

enum class UpgradeOption(val title: String, val description: String) {
    HEAL("체력 회복", "플레이어의 체력을 최대치의 50%만큼 회복합니다."),
    AUTOGUN_DAMAGE("자동 소총 공격력", "자동 소총의 피해량이 증가합니다."),
    AUTOGUN_DELAY("자동 소총 연사력", "자동 소총의 발사 주기가 짧아집니다."),
    GARLIC_RANGE("마늘 범위 증가", "마늘의 타격 반경이 넓어집니다."),
    GARLIC_DELAY("마늘 타격 빈도", "마늘의 틱 데미지 주기가 짧아집니다."),
    SPEAR_DAMAGE("관통 창 공격력", "관통 창의 피해량이 증가합니다."),
    SPEAR_RANGE("관통 창 사거리", "관통 창의 비행 거리가 늘어납니다."),
    SPEAR_DELAY("관통 창 쿨타임", "관통 창의 투척 주기가 짧아집니다.");

    companion object {
        fun getRandomOptions(): List<UpgradeOption> {
            // 8개의 옵션 중 중복 없이 무작위로 3개를 셔플하여 뽑아내는 객관적 수식
            return values().toList().shuffled().take(3)
        }
    }
}