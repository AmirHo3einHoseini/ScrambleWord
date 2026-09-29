package com.example.scrambleword.feature.practice.ui

import android.util.Log.i
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.PracticeConfig
import com.example.scrambleword.feature.practice.state.PracticeSetupUiState
import com.example.scrambleword.feature.practice.viewmodel.PracticeSetupViewModel

@Composable
fun PracticeSetupScreen(
    onBack: () -> Unit,
    onStart: (PracticeConfig) -> Unit,
    viewModel: PracticeSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    PracticeSetupContent(
        uiState = uiState,
        onBack = onBack,
        onLanguageSelected = viewModel::onLanguageSelected,
        onCategorySelected = viewModel::onCategorySelected,
        onStartClick = {
            if (uiState.canStart) {
                onStart(viewModel.currentConfig())

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeSetupContent(
    uiState: PracticeSetupUiState,
    onBack: () -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onStartClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("شروع تمرین") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.background) {
                Button(
                    onClick = onStartClick,
                    enabled = uiState.canStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp, top = 8.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (uiState.canStart) {
                                    Modifier
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(50),
                            color = if (uiState.canStart)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "شروع تمرین",
                                    color = if (uiState.canStart)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (uiState.canStart)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "زبان",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Language.entries.forEach { language ->
                    val selected = uiState.selectedLanguage == language
                    FilterChip(
                        selected = selected,
                        onClick = { onLanguageSelected(language) },
                        label = {
                            Text("${language.flag} ${language.code.uppercase()}")
                        }
                    )
                }
            }

            Text(
                text = "انتخاب دسته‌بندی",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val categoryItems = buildList {
                add(CategoryChipItem(null, "همه کلمات", uiState.totalCount))
                uiState.categoryCounts.forEach {
                    add(CategoryChipItem(it.category, it.category, it.count))
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp), // داخل Column+scroll؛ ارتفاع محدود
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = true
            ) {
                items(categoryItems) { item ->
                    CategoryCard(
                        title = item.title,
                        count = item.count,
                        selected = uiState.selectedCategory == item.category,
                        onClick = { onCategorySelected(item.category) }
                    )
                }
            }

            Text(
                text = "نوع تمرین",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "املای کلمه",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "کلمه را از حروف به‌هم‌ریخته بسازید",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                if (!uiState.canStart) {
                    Text(
                        text = "با این فیلتر هنوز کلمه‌ای نیست. اول از بخش افزودن، کلمه ثبت کن.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "${uiState.selectedCount} کلمه برای این تمرین",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


private data class CategoryChipItem(
    val category: String?, // null = همه
    val title: String,
    val count: Int
)

@Composable
private fun CategoryCard(
    title: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (selected) null else BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Preview(showBackground = true, locale = "fa")
@Composable
fun previewPcticeSetup() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        WordScrambleTheme(darkTheme = true) {
            PracticeSetupContent(
                uiState = PracticeSetupUiState(),
                onBack = {},
                onLanguageSelected = {},
                onCategorySelected = {},
                onStartClick = {}
            )
        }
    }

}