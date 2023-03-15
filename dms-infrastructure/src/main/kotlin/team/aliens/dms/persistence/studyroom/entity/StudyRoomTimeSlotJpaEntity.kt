package team.aliens.dms.persistence.studyroom.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "tbl_study_room_time_slot",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("school_id", "name"))
    ]
)
class StudyRoomTimeSlotJpaEntity(

    override val id: UUID?,

    @Column(columnDefinition = "DARETIME(6)", nullable = false)
    val startTime: LocalDateTime,

    @Column(columnDefinition = "DARETIME(6)", nullable = false)
    val endTime: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

) : BaseUUIDEntity(id)
