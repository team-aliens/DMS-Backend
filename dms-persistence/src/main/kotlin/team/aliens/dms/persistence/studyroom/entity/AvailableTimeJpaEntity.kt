package team.aliens.dms.persistence.studyroom.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalTime
import java.util.UUID

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
