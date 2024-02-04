package team.aliens.dms.persistence.outing.entity

import jakarta.persistence.Entity
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinColumns
import java.util.UUID
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import jakarta.persistence.Column
import jakarta.persistence.FetchType

@Entity
@Table(name = "tbl_outing_application")
class OutingApplicationJpaEntity (

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    override val createdAt: LocalDateTime,

    @Column(columnDefinition = "DATE", nullable = false)
    val outAt: LocalDate,

    @Column(columnDefinition = "TIME", nullable = false)
    val outingTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val arrivalTime: LocalTime,

    @Column(columnDefinition = "VARCHAR(9)", nullable = false)
    val status: OutingStatus,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val reason: String,

    @Column(columnDefinition = "VARCHAR(15)", nullable = false)
    val destination: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "title", referencedColumnName = "title"),
        JoinColumn(name = "outing_type_school_id", referencedColumnName = "school_id"),
    )
    val title: OutingTypeJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?

) : BaseEntity(id)
