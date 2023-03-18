package team.aliens.dms.persistence.studyroom.entity

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity
@Table(name = "tbl_study_room_time_slot")
class StudyRoomTimeSlotJpaEntity(

    @EmbeddedId
    val id: StudyRoomTimeSlotJpaEntityId,

    @MapsId("studyRoomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id", columnDefinition = "BINARY(16)", nullable = false)
    val studyRoom: StudyRoomJpaEntity?,

    @MapsId("timeSlotId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", columnDefinition = "BINARY(16)", nullable = false)
    val timeSlot: TimeSlotJpaEntity?,

)

@Embeddable
data class StudyRoomTimeSlotJpaEntityId(

    @Column
    val studyRoomId: UUID,

    @Column
    val timeSlotId: UUID,

) : Serializable
