package team.aliens.dms.persistence.user.entity

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_user")
class UserJpaEntity(

    override val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    val accountId: String,

    @Column(columnDefinition = "CHAR(60)", nullable = false)
    val password: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(7)", nullable = false)
    val authority: Authority,

    override val createdAt: LocalDateTime,

    @Column(columnDefinition = "DATETIME")
    val deletedAt: LocalDateTime?

) : BaseEntity()