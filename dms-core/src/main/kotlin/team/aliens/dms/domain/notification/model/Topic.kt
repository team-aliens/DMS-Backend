package team.aliens.dms.domain.notification.model

enum class TopicGroup(
    val groupName: String
) {
    NOTICE("공지"),
    STUDY_ROOM("자습실"),
    POINT("상벌점"),
    OUTING("외출")
}

enum class Topic(
    val topicGroup: TopicGroup,
    val title: String,
    val content: String
) {
    NOTICE(
        topicGroup = TopicGroup.NOTICE,
        title = "공지 알림",
        content = "기숙사 공지에 대한 알림입니다."
    ),

    STUDY_ROOM_TIME_SLOT(
        topicGroup = TopicGroup.STUDY_ROOM,
        title = "이용 시간 알림",
        content = "자습실 이용 시작 10분 전에 알림을 받을 수 있습니다."
    ),

    STUDY_ROOM_APPLY(
        topicGroup = TopicGroup.STUDY_ROOM,
        title = "신청 시간 알림",
        content = "자습실 신청 시간을 알리는 알림입니다."
    ),

    POINT(
        topicGroup = TopicGroup.POINT,
        title = "상벌점 알림",
        content = "상벌점을 부여 받았을 때 알림을 받을 수 있습니다."
    ),

    OUTING(
        topicGroup = TopicGroup.OUTING,
        title = "외출 알림",
        content = "외출 신청 시 알림을 받을 수 있습니다"
    )
}
