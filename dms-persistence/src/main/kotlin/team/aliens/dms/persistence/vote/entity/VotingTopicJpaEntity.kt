package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_voting_topic")
class VotingTopicJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val topicName: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = true)
    val description: String?,

    @Column(columnDefinition = "DATETIME(6)", nullable = false)
    val startTime: LocalDateTime,

    @Column(columnDefinition = "DATETIME(6)", nullable = false)
    val endTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val voteType: VoteType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", columnDefinition = "BINARY(16)", nullable = false)
    val manager: ManagerJpaEntity,

) : BaseUUIDEntity(id)
