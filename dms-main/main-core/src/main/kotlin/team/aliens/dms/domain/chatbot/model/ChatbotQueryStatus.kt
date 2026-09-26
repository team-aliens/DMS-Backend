package team.aliens.dms.domain.chatbot.model

enum class ChatbotQueryStatus {
    // LLM 이 답변을 생성했다
    ANSWERED,

    // 검색된 청크가 없어 LLM 을 호출하지 않고 기본 안내 문구로 답했다
    NO_CONTEXT,

    // 임베딩·검색·생성 중 하나가 실패했다
    FAILED
}
