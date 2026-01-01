package team.aliens.dms.persistence.volunteer.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID

@Entity
@Table(name = "tbl_volunteer")
class VolunteerJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT", nullable = false)
    val maxScore: Int,

    @Column(columnDefinition = "INT", nullable = false)
    val minScore: Int,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val maxApplicants: Int,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val availableSex: Sex,

    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    @Enumerated(EnumType.STRING)
    val availableGrade: AvailableGrade,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

) : BaseEntity(id)
