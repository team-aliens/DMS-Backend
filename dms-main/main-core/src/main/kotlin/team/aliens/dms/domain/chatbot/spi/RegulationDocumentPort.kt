package team.aliens.dms.domain.chatbot.spi

interface RegulationDocumentPort {

    /**
     * 기숙사 규정 문서 전문을 불러온다.
     * 1차: 리소스 파일 전문을 그대로 반환(stuffing).
     * 2차: 이 포트의 구현(어댑터)만 검색 기반(RAG)으로 교체하면 유스케이스는 변경되지 않는다.
     */
    fun loadRegulation(): String
}
