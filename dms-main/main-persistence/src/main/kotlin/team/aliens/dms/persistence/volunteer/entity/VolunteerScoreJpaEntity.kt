package team.aliens.dms.persistence.volunteer.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseEntity
import java.util.UUID

@Entity
@Table(name = "tbl_volunteer_score")
class VolunteerScoreJpaEntity(

    id: UUID?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", columnDefinition = "BINARY(16)", nullable = false)
    val volunteerApplication: VolunteerApplicationJpaEntity,

    @Column(name = "assign_score", nullable = false)
    var assignScore: Int

) : BaseEntity(id)
