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

    @Column(nullable = false)
    val topicName: String,

    @Column(nullable = true)
    val description: String?,

    @Column(nullable = false)
    val startTime: LocalDateTime,

    @Column(nullable = false)
    val endTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    val voteType: VoteType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true)
    val manager: ManagerJpaEntity?,

) : BaseUUIDEntity(id)
