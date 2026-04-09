package team.aliens.dms.domain.daybreak.model

enum class Status (
    val korean: String
){
    PENDING("대기"),
    FIRST_APPROVED("1차 승인"),
    SECOND_APPROVED("2차 승인"),
    REJECTED("거절"),
    EXPIRED("만료")
}