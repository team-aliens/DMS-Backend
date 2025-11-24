package team.aliens.dms.persistence.outbox.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_outbox")
class OutboxJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val id: UUID? = null,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val aggregateType: String,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val eventType: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val payload: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val status: OutboxStatus,

    @Column(nullable = false)
    val retryCount: Int = 0,

    @Column(columnDefinition = "DATETIME(6)", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(columnDefinition = "DATETIME")
    val processedAt: LocalDateTime? = null
)

enum class OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
}
