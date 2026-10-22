package com.example.scrambleword.domain.model.practice

data class WordScoreState(
    val base: Int,
    val penalties: Int = 0,
    val usedHint: Boolean = false,
    val mistakeCount: Int = 0,
    val perfectStreakBefore: Int = 0
) {

    fun currentPoints(): Int {
        var points = base - penalties
        if (usedHint) points -= 5
        return points.coerceAtLeast(0)
    }


    fun finalizeIfPerfect(isPerfect: Boolean): Int {

        var points = currentPoints()
        if (isPerfect) {
            points += 5
            val comboBonus = (perfectStreakBefore + 1).coerceAtMost(10)
            if (perfectStreakBefore > 0) {
                points += (perfectStreakBefore + 1).coerceAtMost(10)
            }
        }
        return points
    }


    fun newWordScoreState(
        difficulty: PracticeDifficulty,
        perfectStreak: Int
    ): WordScoreState{
        return WordScoreState(
            base=difficulty.baseScore(),
            perfectStreakBefore=perfectStreak
        )
    }
}
