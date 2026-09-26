package team.aliens.dms.domain.chatbot.model

// 답변 컨텍스트를 만드는 방식. 토큰 사용량 비교를 위해 두 방식을 모두 남겨둔다
enum class ChatbotAnswerMode {
    // 규정 문서 전문을 system instruction 에 통째로 넣는다
    STUFFING,

    // 질문과 가까운 청크만 검색해서 넣는다
    RAG
}
