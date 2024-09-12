package team.aliens.dms.persistence.volunteer.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.util.UUID

@Entity
@Table(name = "tbl_volunteer_application")
class VolunteerApplicationJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", columnDefinition = "BINARY(16)", nullable = false)
    val volunteer: VolunteerJpaEntity?,

    @Column(columnDefinition = "BIT(1)", nullable = false)
    val approved: Boolean,

) : BaseEntity(id)
