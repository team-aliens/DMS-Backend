package team.aliens.dms.domain.chatbot.model

enum class ChatbotQueryStatus {
    // LLM 이 답변을 성공적으로 생성 후 답변
    ANSWERED,

    // 검색된 청크가 없어 기본 안내 문구로 답변
    NO_CONTEXT,

    // 임베딩·검색·생성 중 실패
    FAILED
}
