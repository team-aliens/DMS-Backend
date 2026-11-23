package team.aliens.dms.domain.notification.spi

interface NotificationOutboxPort :
    QueryNotificationOutboxPort,
    CommandNotificationOutboxPort
