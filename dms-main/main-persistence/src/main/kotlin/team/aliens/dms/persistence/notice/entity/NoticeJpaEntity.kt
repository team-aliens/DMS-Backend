package team.aliens.dms.persistence.notice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_notice")
class NoticeJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    val manager: ManagerJpaEntity?,

    @Column(length = 100, nullable = false)
    val title: String,

    @Column(length = 1000, nullable = false)
    val content: String,

    override val createdAt: LocalDateTime,

    @Column(nullable = false)
    val updatedAt: LocalDateTime = createdAt

) : BaseEntity(id)
