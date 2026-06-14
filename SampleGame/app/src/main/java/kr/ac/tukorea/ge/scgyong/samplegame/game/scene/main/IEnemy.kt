package kr.ac.tukorea.ge.scgyong.samplegame.game.scene.main

interface IEnemy {
    val worldX: Float
    val worldY: Float
    var isDead: Boolean // 추가: 사망 상태 플래그
    fun takeDamage(amount: Int)
}