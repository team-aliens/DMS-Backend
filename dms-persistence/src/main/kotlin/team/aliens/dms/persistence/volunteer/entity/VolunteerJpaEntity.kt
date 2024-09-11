package team.aliens.dms.persistence.volunteer.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.GradeCondition
import team.aliens.dms.persistence.BaseEntity
import java.util.UUID

@Entity
@Table(name = "tbl_volunteer")
class VolunteerJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val name: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val content: String,

    @Column(columnDefinition = "INT", nullable = false)
    val score: Int,

    @Column(columnDefinition = "INT", nullable = false)
    val optionalScore: Int,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val maxApplicants: Int,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val sexCondition: Sex,

    @Column(columnDefinition = "VARCHAR(14)", nullable = false)
    @Enumerated(EnumType.STRING)
    val gradeCondition: GradeCondition
) : BaseEntity(id)
