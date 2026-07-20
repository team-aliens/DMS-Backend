package team.aliens.dms.contract.model.notification

enum class TopicGroup(
    val groupName: String
) {
    NOTICE("공지"),
    POINT("상벌점"),
    DAYBREAK_STUDY_APPLICATION("새벽 자습 신청")
}

enum class Topic(
    val topicGroup: TopicGroup
) {
    NOTICE(
        topicGroup = TopicGroup.NOTICE
    ),

    POINT(
        topicGroup = TopicGroup.POINT
    ),

    DAYBREAK_STUDY_APPLICATION(
        topicGroup = TopicGroup.DAYBREAK_STUDY_APPLICATION
    )
}
