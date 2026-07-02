package team.aliens.dms.thirdparty.ai.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import team.aliens.dms.thirdparty.ai.client.dto.GroqChatCompletionRequest
import team.aliens.dms.thirdparty.ai.client.dto.GroqChatCompletionResponse

/**
 * Groq 는 OpenAI 호환 chat/completions 엔드포인트를 제공한다.
 * 인증은 Authorization: Bearer <api-key> 헤더로 전달한다.
 */
@FeignClient(name = "groq-feign-client", url = "\${groq.base-url}")
interface GroqClient {

    @PostMapping("/openai/v1/chat/completions")
    fun chatCompletion(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody request: GroqChatCompletionRequest
    ): GroqChatCompletionResponse
}
