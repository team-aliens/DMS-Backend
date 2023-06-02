package team.aliens.dms.domain.notification.model

import team.aliens.dms.domain.notice.model.Notice

sealed class Notification(
    val topic: Topic,
    val title: String,
    val content: String,
    val threadId: String
) {
    class NoticeNotification(
        notice: Notice
    ) : Notification(
        topic = Topic.NOTICE,
        title = "${notice.title}",
        content = "기숙사 공지가 등록되었습니다.",
        threadId = notice.id.toString()
    )
}
