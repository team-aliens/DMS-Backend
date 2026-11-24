package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.event.NotificationOutboxSender
import java.util.concurrent.TimeUnit

// 복구 경로: 커밋 직후 발송이 못 끝낸 outbox 를 다시 보낸다.
// Quartz 는 밀린 실행을 몰아서 돌려 재시도 횟수를 소진하므로, 끝난 뒤부터 1분을 세는 fixedDelay 를 쓴다
@Component
class NotificationOutboxScheduler(
    private val notificationOutboxSender: NotificationOutboxSender
) {

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    fun resend() {
        notificationOutboxSender.resend()
    }
}
