package team.aliens.dms.domain.chatbot

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.common.extension.readText
import team.aliens.dms.domain.chatbot.dto.ChatbotAnswerResponse
import team.aliens.dms.domain.chatbot.dto.ChatbotDocumentIdResponse
import team.aliens.dms.domain.chatbot.dto.request.AskChatbotWebRequest
import team.aliens.dms.domain.chatbot.usecase.AskChatbotUseCase
import team.aliens.dms.domain.chatbot.usecase.RemoveChatbotDocumentUseCase
import team.aliens.dms.domain.chatbot.usecase.UploadChatbotDocumentUseCase
import java.time.LocalDate
import java.util.UUID

@Validated
@RequestMapping("/chatbots")
@RestController
class ChatbotWebAdapter(
    private val askChatbotUseCase: AskChatbotUseCase,
    private val uploadChatbotDocumentUseCase: UploadChatbotDocumentUseCase,
    private val removeChatbotDocumentUseCase: RemoveChatbotDocumentUseCase
) {

    @PostMapping("/questions")
    fun askQuestion(@RequestBody @Valid request: AskChatbotWebRequest): ChatbotAnswerResponse {
        return askChatbotUseCase.execute(request.question, request.mode)
    }

    // md/txt 파일 하나. 같은 이름의 파일을 다시 올리면 그 문서가 교체된다
    @PostMapping("/documents")
    fun uploadDocument(
        @RequestPart @NotNull file: MultipartFile?,
        @RequestParam("published_at", required = false) publishedAt: LocalDate?
    ): ChatbotDocumentIdResponse {
        return uploadChatbotDocumentUseCase.execute(
            fileName = file!!.originalFilename!!,
            content = file.readText(),
            // 개정일은 날짜만 받고, 도메인·컬럼은 시각까지 갖는다(자정 기준)
            publishedAt = (publishedAt ?: LocalDate.now()).atStartOfDay()
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/documents/{document-id}")
    fun removeDocument(@PathVariable("document-id") @NotNull documentId: UUID) {
        removeChatbotDocumentUseCase.execute(documentId)
    }
}
