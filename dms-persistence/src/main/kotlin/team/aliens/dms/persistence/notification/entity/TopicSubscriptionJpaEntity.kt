package team.aliens.dms.persistence.notification.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import team.aliens.dms.domain.notification.model.Topic
import java.io.Serializable
import java.util.UUID

@Entity
@Table(name = "tbl_topic_subscription")
class TopicSubscriptionJpaEntity(

    @EmbeddedId
    val id: TopicSubscriptionJpaEntityId,

    @MapsId("deviceTokenId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_token_id", columnDefinition = "BINARY(16)", nullable = false)
    val deviceToken: DeviceTokenJpaEntity,

    @Column(columnDefinition = "BIT(1)", nullable = false)
    val isSubscribed: Boolean

)

@Embeddable
data class TopicSubscriptionJpaEntityId(

    @Column
    val deviceTokenId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val topic: Topic

) : Serializable
