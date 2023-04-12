package team.aliens.dms.persistence.notice.entity

import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_notice")
class NoticeJpaEntity(

    override val id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", columnDefinition = "BINARY(16)", nullable = false)
    val manager: ManagerJpaEntity?,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val title: String,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    val content: String,

    override val createdAt: LocalDateTime,

    @Column(columnDefinition = "DATETIME", nullable = false)
    val updatedAt: LocalDateTime = createdAt

) : BaseEntity(id)
