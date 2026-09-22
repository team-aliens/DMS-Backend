package team.aliens.dms.persistence.notification.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import team.aliens.dms.contract.model.notification.PointDetailTopic
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.persistence.BaseUUIDEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_notification_of_user")
class NotificationOfUserJpaEntity(

    id: UUID?,

    @Column(nullable = false)
    val userId: UUID?,

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    val topic: Topic,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = true)
    val pointDetailTopic: PointDetailTopic?,

    @Column(length = 500)
    val linkIdentifier: String?,

    @Column(length = 500, nullable = false)
    val title: String,

    @Column(length = 500, nullable = false)
    val content: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    @ColumnDefault("false")
    val isRead: Boolean

) : BaseUUIDEntity(id)
