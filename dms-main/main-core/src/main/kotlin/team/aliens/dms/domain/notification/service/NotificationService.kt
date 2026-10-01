package team.aliens.dms.domain.notification.service

import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.annotation.Service

// 다른 도메인 서비스와 달리 @Transactional 이 붙어 있다.
// NotificationEventHandler·DeviceTokenEventHandler 가 @Async + AFTER_COMMIT 으로 호출해 유스케이스 트랜잭션 밖이기 때문이다
@Service
@Transactional
class NotificationService(
    getNotificationService: GetNotificationService,
    checkNotificationService: CheckNotificationService,
    commandNotificationService: CommandNotificationService
) : GetNotificationService by getNotificationService,
    CheckNotificationService by checkNotificationService,
    CommandNotificationService by commandNotificationService
