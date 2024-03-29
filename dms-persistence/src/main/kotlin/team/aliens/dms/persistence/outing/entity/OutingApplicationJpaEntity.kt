package team.aliens.dms.persistence.outing.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinColumns
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "tbl_outing_application")
class OutingApplicationJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    override val createdAt: LocalDateTime,

    @Column(columnDefinition = "DATE", nullable = false)
    val outingDate: LocalDate,

    @Column(columnDefinition = "TIME", nullable = false)
    val outingTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val arrivalTime: LocalTime,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(9)", nullable = false)
    val status: OutingStatus,

    @Column(columnDefinition = "VARCHAR(100)", nullable = true)
    val reason: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "outing_type_title", referencedColumnName = "title", nullable = false),
        JoinColumn(name = "outing_type_school_id", referencedColumnName = "school_id", columnDefinition = "BINARY(16)", nullable = false),
    )
    val outingType: OutingTypeJpaEntity?

) : BaseEntity(id)
