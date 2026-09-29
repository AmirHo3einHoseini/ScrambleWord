package com.example.scrambleword.domain.usecase.practice

import com.example.scrambleword.domain.model.CategoryCount
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCategoryCountsUseCase @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(language: Language): Flow<List<CategoryCount>> {
        return repository.observeCategoryCounts(language)
    }
}