package team.aliens.dms.persistence.remain.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_remain_available_time")
class RemainAvailableTimeJpaEntity(

    @Id
    val id: UUID,

    @MapsId("schoolId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val startDayOfWalk: DayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalDate,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val endDayOfWalk: DayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalDate

)