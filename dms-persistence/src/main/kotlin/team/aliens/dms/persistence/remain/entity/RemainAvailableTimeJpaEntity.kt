package team.aliens.dms.persistence.remain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "tbl_remain_available_time")
class RemainAvailableTimeJpaEntity(

    @Id
    @Column(name = "school_id")
    val id: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val startDayOfWeek: DayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalTime,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val endDayOfWeek: DayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalTime

)
