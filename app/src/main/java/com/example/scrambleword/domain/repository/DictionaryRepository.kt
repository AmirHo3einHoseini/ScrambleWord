package com.example.scrambleword.domain.repository

import com.example.scrambleword.domain.model.DictionaryInfo
import com.example.scrambleword.domain.model.Language

interface DictionaryRepository {

    /**
     * اطلاعات یک کلمه را از دیکشنری آنلاین دریافت می‌کند.
     * @param word کلمه‌ای که باید جستجو شود
     * @param language زبان مورد نظر
     * @return Result حاوی DictionaryInfo در صورت موفقیت، یا خطا
     */
    suspend fun fetchWordInfo(
        word: String,
        language: Language
    ): Result<DictionaryInfo>

}