package team.aliens.dms.persistence.studyroom.entity

import java.time.LocalTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Entity
@Table(name = "tbl_study_room_available_time")
class AvailableTimeJpaEntity(

    @Id
    val schoolId: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "TIME", nullable = false)
    val startAt: LocalTime,

    @Column(columnDefinition = "TIME", nullable = false)
    val endAt: LocalTime

)