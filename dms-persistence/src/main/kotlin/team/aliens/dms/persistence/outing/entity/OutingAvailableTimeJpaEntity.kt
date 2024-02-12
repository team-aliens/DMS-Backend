package team.aliens.dms.persistence.outing.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "tbl_outing_available_time")
class OutingAvailableTimeJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "TIME", nullable = false)
    val outingTime: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val arrivalTime: LocalTime,

    @Column(columnDefinition = "BIT(1)", nullable = false)
    val enabled: Boolean,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek

) : BaseUUIDEntity(id)
