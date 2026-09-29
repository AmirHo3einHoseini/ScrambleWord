package com.example.scrambleword.feature.wordlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrambleword.core.audio.PronunciationPlayer
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.feature.wordlist.WordListItem
import com.example.scrambleword.feature.wordlist.uistate.WordListEmptyReason
import com.example.scrambleword.feature.wordlist.uistate.WordListUiState
import com.example.scrambleword.feature.wordlist.viewmodel.WordListViewModel


@Composable
fun WordListScreen(
    onBack: () -> Unit,
    onAddWord: (prefillWord: String?, prefillLanguage: Language?) -> Unit,
    onWordClick: (Long) -> Unit = {},
    viewModel: WordListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val player = remember { PronunciationPlayer() }

    DisposableEffect(Unit) {
        onDispose { player.stop() }
    }

    WordListContent(
        uiState = uiState,
        onBack = onBack,
        onAddWord = onAddWord,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onLanguageSelected = viewModel::onLanguageSelected,
        onWordClick = onWordClick,
        onPlayAudio = { url -> player.play(url) }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordListContent(
    uiState: WordListUiState,
    onBack: () -> Unit,
    onAddWord: (prefillWord: String?, prefillLanguage: Language?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onLanguageSelected: (Language?) -> Unit,
    onWordClick: (Long) -> Unit,
    onPlayAudio: (String) -> Unit
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }
    var localQuery by rememberSaveable { mutableStateOf(uiState.searchQuery) }
    LaunchedEffect(uiState.searchQuery) {
        if (uiState.searchQuery != localQuery) {
            localQuery = uiState.searchQuery
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("کلمات من") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddWord(null, uiState.selectedLanguage) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "افزودن کلمه")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = localQuery,
                    onValueChange = { newValue ->
                        localQuery = newValue
                        onSearchQueryChange(newValue)
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text("جست و جوی کلمه")
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (localQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    localQuery = ""
                                    onSearchQueryChange("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "پاک کردن"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Box {
                    Surface(
                        onClick = { languageMenuExpanded = true },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        modifier = Modifier.size(52.dp)
                    ) {

                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = uiState.selectedLanguage?.flag ?: "🌐",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = languageMenuExpanded,
                        onDismissRequest = { languageMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("همه 🌐") },
                            onClick = {
                                onLanguageSelected(null)
                                languageMenuExpanded = false
                            }
                        )
                        Language.entries.forEach { language ->
                            DropdownMenuItem(
                                text = {
                                    Text("${language.flag} ${language.code.uppercase()}")
                                },
                                onClick = {
                                    onLanguageSelected(language)
                                    languageMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ///
            if (uiState.emptyReason != WordListEmptyReason.NONE) {
                EmptyState(
                    reason = uiState.emptyReason,
                    searchQuery = uiState.searchQuery,
                    selectedLanguage = uiState.selectedLanguage,
                    onAddWord = onAddWord
                )

            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(
                        items = uiState.items,
                        key = { item ->
                            when (item) {
                                is WordListItem.LanguageHeader -> "header_${item.language.code}"
                                is WordListItem.WordRow -> "word_${item.word.id}"
                            }
                        }
                    ) { item ->
                        when (item) {
                            is WordListItem.LanguageHeader -> {
                                LanguageHeaderRow(item)
                            }

                            is WordListItem.WordRow -> {
                                WordCard(
                                    word = item.word,
                                    onClick = { onWordClick(item.word.id) },
                                    onPlayAudio = { item.word.audioUrl?.let(onPlayAudio) }
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}

@Composable
fun EmptyState(
    reason: WordListEmptyReason,
    searchQuery: String,
    selectedLanguage: Language?,
    onAddWord: (prefillWord: String?, prefillLanguage: Language?) -> Unit
) {
    val (title, button) = when (reason) {
        WordListEmptyReason.NO_WORDS_AT_ALL ->
            "هنوز کلمه ای ذخیره نشده." to "افزودن اولین کلمه"

        WordListEmptyReason.NO_WORDS_FOR_LANGUAGE -> {
            val name = selectedLanguage?.displayName?.uppercase() ?: ""
            " هنوز کلمه  ${name}وارد نکردی " to "وارد کردن اولین کلمه $name"
        }

        WordListEmptyReason.NO_SEARCH_RESULT ->
            "این کلمه را هنوز وارد نکردی" to
                    if (searchQuery.isNotBlank()) "وارد کردن «$searchQuery»"
                    else "وارد کردن کلمه"

        else -> "" to ""

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = {
                when (reason) {
                    WordListEmptyReason.NO_SEARCH_RESULT ->
                        onAddWord(searchQuery.trim().ifBlank { null }, selectedLanguage)

                    WordListEmptyReason.NO_WORDS_FOR_LANGUAGE ->
                        onAddWord(null, selectedLanguage)

                    else -> onAddWord(null, null)
                }
            }
        ) {
            Text(button)
        }
    }


}

@Composable
private fun LanguageHeaderRow(item: WordListItem.LanguageHeader) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = item.language.flag, style = MaterialTheme.typography.titleMedium)
        Text(
            text = item.language.code.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "(${item.count})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

}


@Composable
private fun WordCard(
    word: Word,
    onClick: () -> Unit,
    onPlayAudio: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = word.word,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!word.phonetic.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.phonetic,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            if (!word.audioUrl.isNullOrBlank()) {
                IconButton(onClick = onPlayAudio) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "پخش",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }


}

@Preview(showBackground = true, locale = "fa")
@Composable
fun previewWordListContent() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        WordScrambleTheme(darkTheme = false) {
            WordListContent(
                WordListUiState(),
                onBack = {},
                onAddWord = { string, language -> },
                onWordClick = {},
                onPlayAudio = {},
                onLanguageSelected = {},
                onSearchQueryChange = {}


            )
        }
    }
}

