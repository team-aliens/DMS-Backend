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
    val topicGroup: TopicGroup
) {
    NOTICE(
        topicGroup = TopicGroup.NOTICE
    ),

    STUDY_ROOM_TIME_SLOT(
        topicGroup = TopicGroup.STUDY_ROOM
    ),

    STUDY_ROOM_APPLY(
        topicGroup = TopicGroup.STUDY_ROOM
    ),

    POINT(
        topicGroup = TopicGroup.POINT
    ),

    OUTING(
        topicGroup = TopicGroup.OUTING
    )
}
