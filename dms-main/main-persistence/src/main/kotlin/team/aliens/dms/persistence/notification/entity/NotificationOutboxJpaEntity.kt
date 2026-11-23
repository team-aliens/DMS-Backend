package team.aliens.dms.persistence.notification.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import team.aliens.dms.contract.model.notification.PointDetailTopic
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.NotificationOutboxStatus
import team.aliens.dms.persistence.BaseUUIDEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_notification_outbox")
class NotificationOutboxJpaEntity(

    id: UUID?,

    // 발송 대상 전체를 행 하나에 담는다. 재시도 땐 실패한 유저만 남긴다
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "user_ids", nullable = false)
    val userIds: List<UUID>,

    @Column(name = "school_id", nullable = false)
    val schoolId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    val topic: Topic,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val pointDetailTopic: PointDetailTopic?,

    @Column(length = 500)
    val linkIdentifier: String?,

    @Column(length = 500, nullable = false)
    val title: String,

    @Column(length = 500, nullable = false)
    val content: String,

    @Column(length = 500, nullable = false)
    val threadId: String,

    @Column(nullable = false)
    val isSaveRequired: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    val status: NotificationOutboxStatus,

    @Column(nullable = false)
    val retryCount: Int,

    // 알림이 생긴 시각. 재시도로 늦게 발송돼도 알림함에는 이 시각이 남는다
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime

) : BaseUUIDEntity(id)
