package sunfusheng.famous.quotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sunfusheng.famous.quotes.data.Quote
import sunfusheng.famous.quotes.data.QuoteMeta
import sunfusheng.famous.quotes.data.QuoteRepository
import kotlin.coroutines.cancellation.CancellationException

data class QuoteUiState(
    val isLoading: Boolean = false,
    val quote: Quote? = null,
    val meta: QuoteMeta? = null,
    val errorMessage: String? = null,
)

class QuoteViewModel(
    private val repository: QuoteRepository = QuoteRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuoteUiState())
    val uiState: StateFlow<QuoteUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchQuote()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        quote = response.data,
                        meta = response.meta,
                        errorMessage = null,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "网络请求失败，请稍后重试",
                    )
                }
            }
        }
    }
}
