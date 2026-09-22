package team.aliens.dms.persistence.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_user")
@SQLRestriction("deleted_at is null")
class UserJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(length = 20, nullable = false, unique = true)
    val accountId: String,

    @Column(length = 60, nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    val authority: Authority,

    override val createdAt: LocalDateTime,

    val deletedAt: LocalDateTime?,

) : BaseEntity(id)
