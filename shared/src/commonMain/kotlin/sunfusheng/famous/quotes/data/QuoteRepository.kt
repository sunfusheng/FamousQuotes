package sunfusheng.famous.quotes.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import sunfusheng.famous.quotes.network.createQuoteHttpClient

class QuoteRepository(
    private val client: HttpClient = createQuoteHttpClient(),
) {

    suspend fun fetchQuote(): QuoteResponse {
        val response = client.get(ApiConfig.QUOTE_URL).body<QuoteResponse>()
        if (response.code != 0) {
            throw IllegalStateException(response.msg.ifBlank { "请求失败（code=${response.code}）" })
        }
        return response
    }
}

object ApiConfig {
    const val QUOTE_URL = "https://api.ruseo.cn/api/mingren"
}
