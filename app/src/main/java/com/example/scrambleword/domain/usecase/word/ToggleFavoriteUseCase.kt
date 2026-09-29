package com.example.scrambleword.domain.usecase.word

import androidx.compose.runtime.currentRecomposeScope
import com.example.scrambleword.domain.repository.WordRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: WordRepository
) {

    suspend operator fun invoke(id: Long, currentFavoriteState: Boolean) {
        repository.setFavorite(id, !currentFavoriteState)
    }
}