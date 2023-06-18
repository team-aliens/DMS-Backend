package team.aliens.dms.domain.notification.model


enum class TopicGroup {
    NOTICE,
    STUDY_ROOM
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
    );
}