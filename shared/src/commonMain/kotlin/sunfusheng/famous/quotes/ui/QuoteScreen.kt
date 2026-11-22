package sunfusheng.famous.quotes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import sunfusheng.famous.quotes.data.Quote
import sunfusheng.famous.quotes.data.QuoteMeta

internal val Accent = Color(0xFFA2673C)
internal val Ink = Color(0xFF2E2620)
internal val MutedInk = Color(0xFF8A7B6C)

private val ScreenBackground = Brush.verticalGradient(listOf(Color(0xFFFDF9F3), Color(0xFFF2E8DA)))

@Composable
fun QuoteScreen(
    category: String = "名人名言",
    onOpenSettings: () -> Unit = {},
    viewModel: QuoteViewModel = viewModel { QuoteViewModel() },
) {
    val state by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().background(ScreenBackground).safeContentPadding(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            QuoteTopBar(onOpenSettings = onOpenSettings)
            QuoteContent(
                state = state,
                category = category,
                onRefresh = viewModel::refresh,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun QuoteContent(
    state: QuoteUiState,
    category: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = AppInfo.SLOGAN,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
                color = MutedInk,
            )
            QuoteCard(state = state, category = category)
            state.errorMessage?.let { message ->
                ErrorBanner(message = message)
            }
            RefreshButton(isLoading = state.isLoading, onClick = onRefresh)
        }
    }
}

@Composable
private fun QuoteTopBar(onOpenSettings: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = AppInfo.NAME,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
        )
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = SettingsIcon,
                contentDescription = "设置",
                modifier = Modifier.size(22.dp),
                tint = Ink,
            )
        }
    }
}

@Composable
private fun QuoteCard(state: QuoteUiState, category: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 22.dp),
        ) {
            if (state.quote == null && state.isLoading) {
                Text(
                    text = "正在获取名言…",
                    fontSize = 18.sp,
                    lineHeight = 30.sp,
                    color = MutedInk,
                )
            } else {
                Text(
                    text = state.quote?.content ?: "暂无内容，点一下刷新试试",
                    fontSize = 19.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Ink,
                )
            }
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFFEDE2D3))
            Spacer(Modifier.height(14.dp))
            Text(
                text = category,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = MutedInk,
            )
        }
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFBE9E7),
    ) {
        Text(
            text = "加载失败：$message",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = Color(0xFFB3261E),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun RefreshButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(27.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Accent,
            contentColor = Color.White,
            disabledContainerColor = Accent.copy(alpha = 0.55f),
            disabledContentColor = Color.White,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color.White,
            )
            Spacer(Modifier.width(10.dp))
            Text(text = "刷新中…", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        } else {
            Text(text = "刷新一句", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview
@Composable
private fun QuoteContentPreview() {
    val state = QuoteUiState(
        quote = Quote(
            content = "年轻时我们总是在开始时毫无所谓，在结束时痛彻心扉。而长大后成熟的我们可能避免了幼稚的伤害，却也错过了开始的勇气。 ——九夜茴 《匆匆那年》",
            source = "remote",
            cacheCount = 743,
        ),
        meta = QuoteMeta(route = "mingren", latencyMs = 59),
    )
    Box(
        modifier = Modifier.fillMaxSize().background(ScreenBackground).safeContentPadding(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            QuoteTopBar(onOpenSettings = {})
            QuoteContent(
                state = state,
                category = "名人名言",
                onRefresh = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
