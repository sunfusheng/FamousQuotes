package sunfusheng.famous.quotes.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    val code: Int,
    val msg: String = "",
    val data: Quote,
    val meta: QuoteMeta? = null,
)

@Serializable
data class Quote(
    val content: String,
    val source: String = "",
    @SerialName("cache_count")
    val cacheCount: Int = 0,
)

@Serializable
data class QuoteMeta(
    val route: String = "",
    @SerialName("latency_ms")
    val latencyMs: Long = 0,
)
