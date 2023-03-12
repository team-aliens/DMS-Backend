package team.aliens.dms.persistence.studyroom.entity

import team.aliens.dms.persistence.BaseUUIDEntity
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
    name = "tbl_study_room",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("study_room_info_id", "time_slot_id"))
    ]
)
class StudyRoomJpaEntity(

    override val id: UUID?,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val inUseHeadcount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_info_id", columnDefinition = "BINARY(16)", nullable = true)
    val studyRoomInfo: StudyRoomInfoJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", columnDefinition = "BINARY(16)", nullable = true)
    val timeSlot: StudyRoomTimeSlotJpaEntity?,

) : BaseUUIDEntity(id)
