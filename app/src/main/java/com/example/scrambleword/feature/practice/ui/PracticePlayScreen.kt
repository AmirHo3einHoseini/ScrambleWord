package com.example.scrambleword.feature.practice.ui

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrambleword.core.audio.PronunciationPlayer
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.domain.model.practice.CubeLetter
import com.example.scrambleword.domain.model.practice.SlotsStroke
import com.example.scrambleword.domain.model.practice.stroke
import com.example.scrambleword.feature.practice.state.PracticePlayUiState
import com.example.scrambleword.feature.practice.viewmodel.PracticePlayViewModel


@Composable
fun PracticePlayScreen(
    onBack: () -> Unit,
    viewModel: PracticePlayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val player = remember { PronunciationPlayer() }

    DisposableEffect(Unit) {
        onDispose { player.stop() }
    }

    PracticePlayContent(
        uiState = uiState,
        onBack = onBack,
        onBankCubeClick = viewModel::onBankCubeClick,
        onSlotClick = viewModel::onSlotClick,
        onNextWord = viewModel::onNextWord,
        onRetryCurrent = viewModel::onRetryCurrent,
        onPlayAudio = {
            uiState.audioUrl?.let { player.play(it) }
        },
        onHintClick = viewModel::onHintClick
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticePlayContent(
    uiState: PracticePlayUiState,
    onBack: () -> Unit,
    onBankCubeClick: (Int) -> Unit,
    onSlotClick: (Int) -> Unit,
    onNextWord: () -> Unit,
    onRetryCurrent: () -> Unit,
    onPlayAudio: () -> Unit,
    onHintClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.totalCount > 0) {
                            Text("${uiState.currentIndex + 1} /${uiState.totalCount}")

                        } else {
                            Text("تمرین")
                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        Text(
                            text = " امتیاز : ${uiState.sessionScore} ",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                    }


                },
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
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            uiState.isSessionFinished -> {
                SessionSummaryContent(
                    sessionScore = uiState.sessionScore,
                    perfectCount = uiState.perfectCount,
                    totalCount = uiState.totalCount,
                    onBack = onBack
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = uiState.meaning,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    if (uiState.showHint && !uiState.hint.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "راهنما: ${uiState.hint}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (uiState.hintButtonVisible && uiState.canUseHint) {
                        TextButton(onClick = onHintClick) {
                            Text("نمایش راهنما (−۵)")
                        }
                    }
                    if (!uiState.audioUrl.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        IconButton(onClick = onPlayAudio) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "پخش تلفظ",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(Modifier.height(28.dp))

                    val cubeSize = when {
                        uiState.slots.size > 12 -> 40.dp
                        uiState.slots.size > 8 -> 46.dp
                        else -> 52.dp
                    }
                    CompositionLocalProvider(
                        LocalLayoutDirection provides LayoutDirection.Ltr
                    ) {

                        FlowRow(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 8
                        ) {
                            uiState.slots.forEach { slot ->
                                val cube = uiState.cubes.find { it.id == slot.cubeId }
                                val stroke = slot.stroke(uiState.cubes)
                                SlotCube(
                                    char = cube?.char,
                                    stroke = stroke,
                                    onClick = { onSlotClick(slot.index) },
                                    cubeSize
                                )
                            }

                        }
                    }
                    Spacer(Modifier.height(36.dp))
                    val bankOrder = uiState.cubes.sortedBy { it.homeIndex }
                    CompositionLocalProvider(
                        LocalLayoutDirection provides LayoutDirection.Ltr
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 8
                        ) {
                            bankOrder.forEach { cube ->
                                BankCubeSlot(
                                    cube = cube,
                                    onClick = {
                                        if (cube.isInBank) {
                                            onBankCubeClick(cube.id)
                                        }
                                    },
                                    cubeSize
                                )
                            }


                        }
                    }
                    Spacer(Modifier.height(28.dp))
                    if (uiState.isCompleted) {
                        uiState.lastWordPoints?.let { pts ->
                            Text(
                                text = "+$pts",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "درست بود!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = onNextWord,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (uiState.currentIndex + 1 >= uiState.totalCount)
                                    "پایان جلسه"
                                else
                                    "کلمه بعدی"
                            )
                        }
                    } else {
                        TextButton(onClick = onRetryCurrent) {
                            Text("از نو")
                        }
                    }

                }
            }
        }
    }
}


@Composable
private fun SlotCube(
    char: Char?,
    stroke: SlotsStroke,
    onClick: () -> Unit,
    cubeSize: Dp
) {
    val borderColor = when (stroke) {
        SlotsStroke.Empty -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        SlotsStroke.Correct -> Color(0xFF4ADE80)
        SlotsStroke.Wrong -> Color(0xFFF87171)
    }
    val borderWidth = when (stroke) {
        SlotsStroke.Wrong -> 1.dp
        else -> 2.dp
    }

    Box(
        modifier = Modifier
            .size(cubeSize)
            .clip(RoundedCornerShape(12.dp))
            .background(cubeBrush(filled = char != null))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = char != null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (char != null) {
            Text(
                text = char.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }


}

@Composable
private fun BankCubeSlot(
    cube: CubeLetter,
    onClick: () -> Unit,
    cubeSize: Dp
) {
    // جای خالی رزرو می‌شود تا ترتیب homeIndex به‌هم نریزد

    Box(
        modifier = Modifier.size(cubeSize),
        contentAlignment = Alignment.Center
    ) {
        if (cube.isInBank) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(cubeBrush(filled = true))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cube.char.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun cubeBrush(filled: Boolean): Brush {
    val surface = MaterialTheme.colorScheme.surface
    val variant = MaterialTheme.colorScheme.surfaceVariant
    return if (filled) {
        Brush.linearGradient(
            listOf(
                surface.copy(alpha = 1f),
                variant.copy(alpha = 0.95f)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                variant.copy(alpha = 0.35f),
                variant.copy(alpha = 0.2f)
            )
        )
    }
}


@Composable
private fun SessionSummaryContent(
    sessionScore: Int,
    perfectCount: Int,
    totalCount: Int,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "پایان جلسه",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "امتیاز کل: $sessionScore",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "کلمات این جلسه: $totalCount",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Perfect: $perfectCount",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )


        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text("بازگشت")
        }
    }

}


@Preview(showBackground = true, locale = "fa")
@Composable
fun PracticePlayContentPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        WordScrambleTheme {
            PracticePlayContent(
                uiState = PracticePlayUiState(),
                onBack = {},
                onBankCubeClick = {},
                onSlotClick = {},
                onNextWord = {},
                onRetryCurrent = {},
                onPlayAudio = {},
                onHintClick = {}
            )
        }
    }
}