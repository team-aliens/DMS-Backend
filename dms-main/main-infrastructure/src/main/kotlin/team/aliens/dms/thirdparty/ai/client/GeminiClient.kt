package team.aliens.dms.thirdparty.ai.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import team.aliens.dms.thirdparty.ai.client.dto.GeminiGenerateContentRequest
import team.aliens.dms.thirdparty.ai.client.dto.GeminiGenerateContentResponse

@FeignClient(name = "gemini-feign-client", url = "\${gemini.base-url}")
interface GeminiClient {

    @PostMapping("/v1beta/models/{model}:generateContent")
    fun generateContent(
        @PathVariable("model") model: String,
        @RequestHeader("x-goog-api-key") apiKey: String,
        @RequestBody request: GeminiGenerateContentRequest
    ): GeminiGenerateContentResponse
}
